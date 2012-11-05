require "sinatra"
require "pathname"

FEED_FILES = ['small-feed.rss', 'large-feed.rss']
$feed_index = 0

get '/rss' do
  content_type 'application/xml'
  read_next_feed
end

def read_next_feed
  next_feed_path.read
end

def next_feed_path
  path = data_path + FEED_FILES[$feed_index]
  $feed_index = ($feed_index + 1) % FEED_FILES.size
  path
end

def data_path
  Pathname(__FILE__).dirname + 'feeds'
end

