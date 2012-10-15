require "sinatra"
require "pathname"
require "pry"

STREAM_FILE = Pathname(__FILE__).dirname + "stream.mp3" 
# STREAM_FILE = Pathname(__FILE__).dirname + "stream-small.mp3"

CHUNK_SIZE = 4096
DELAY_MEAN = 0.102
DELAY_VARIATION = 0.05
SECOND_DELAY_CHANCE = 0.01
GRAND_DELAY_CHANCE = 0.001
GRAND_DELAY = 40


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

get "/stream" do
  puts "\n * Starting streaming with delay mean: #{DELAY_MEAN}"
  content_type "audio/mpeg"
  stream :keep_open do |out|
    open(STREAM_FILE, "rb") { |io| read_file_with_delays io, out, DELAY_MEAN }
  end
end

def read_file_with_delays(io, out, delay)
  stat = Stat.new
  stat.start
  interrupted = false

  out.callback { interrupted = true }

  while not (io.eof? or interrupted)
    chunk = io.readpartial(4096)
    stat.sample chunk.size
    out << chunk
    print "."
    sleep randomize(delay)
  end
  stat.stop
  out.close
  puts "\nStatistics is: #{stat.bytes_per_sec.to_i}"
end

def randomize(median)
  fluctuated = median + fluctuation + second_delay + grand_delay
end

def second_delay
  chances = rand()
  (chances < SECOND_DELAY_CHANCE) ? 1 : 0
end

def grand_delay
  chances = rand()
  (chances < GRAND_DELAY_CHANCE) ? GRAND_DELAY : 0
end

def fluctuation
  ((DELAY_VARIATION / 2) - (DELAY_VARIATION * rand()))
end
