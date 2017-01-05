require "sinatra"
require "pathname"
require "pry"

require_relative "profiles"

STREAM_FILE = Pathname(__FILE__).dirname + "stream.mp3" 

CHUNK_SIZE = 4096
STREAM_SPEED = 0.102

class Stat
  def initialize
    @bytes_count = 0
  end
  
  def start
    @bytes_count = 0
    @start_time = Time.now
  end
  
  def stop
    @stop_time = Time.now
  end
  
  def sample(count)
    @bytes_count += count
  end
  
  def bytes_per_sec
    @bytes_count / (@stop_time - @start_time)
  end
end

# DELAY_PROFILE = RandomDelayProfile.new(STREAM_SPEED)
DELAY_PROFILE = ConstantDelayProfile.new(STREAM_SPEED)

set :bind, '0.0.0.0'

get "/stream" do
  puts "\n * Starting streaming with speed factor: #{STREAM_SPEED}"
  content_type "audio/mpeg"
  stream :keep_open do |out|
    open(STREAM_FILE, "rb") { |io| read_file_with_delays io, out }
  end
end


def read_file_with_delays(io, out)
  stat = Stat.new
  stat.start
  interrupted = false
  
  out.callback { interrupted = true }
  
  while not (io.eof? or interrupted)
    chunk = io.readpartial(4096)
    stat.sample chunk.size
    out << chunk
    print "."
    DELAY_PROFILE.introduce_delay
  end
  stat.stop
  out.close
  puts "\nStatistics is: #{stat.bytes_per_sec.to_i}"
end

