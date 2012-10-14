require "sinatra"
require "pathname"

STREAM_FILE = Pathname(__FILE__).dirname + "stream.mp3"
DELAY_BETWEEN_CHUNKS = 0.102

get "/stream" do
  puts "Starting streaming with delays: #{DELAY_BETWEEN_CHUNKS}"
  content_type "audio/mpeg"
  stream :keep_open do |out|
    io = open(STREAM_FILE, "rb")
    read_file_with_delays io, out, DELAY_BETWEEN_CHUNKS
  end
end

def read_file_with_delays(io, out, delay)
  while not io.eof?
    out << io.readpartial(4096)
    print "."
    sleep delay
  end
end

