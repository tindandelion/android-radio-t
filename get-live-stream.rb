require "open-uri"

urls = []

10000.times { |i|
  print " #{i} "
  urls << open("http://stream.radio-t.com:8181/stream.m3u") { |io| io.read.chomp }
}

puts

grouped = urls.group_by { |v| v }
grouped.each_pair do |key, value|
  puts "#{key}: #{value.size}"
end

