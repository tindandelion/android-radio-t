require "sinatra"
require 'json'
require 'faker'

MESSAGE_TEXT = <<-EOM
Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
Phasellus laoreet tincidunt ligula, at feugiat lectus ultricies et. 
Donec odio ligula, congue vel venenatis non, scelerisque et ipsum
EOM

get '/data/jsonp' do
  content_type 'application/json'
  compose_chat_stream
end

def compose_chat_stream
  wrap_into_callback compose_chat_json
end

def wrap_into_callback(json)
  "callback_fn(#{json})"
end

def compose_chat_json
  { "records" => initial_records_array }.to_json
end

def initial_records_array
  (1..5).collect { |i| create_chat_record }
end

def create_chat_record
  {
    'msg' => Faker::Lorem.sentence,
    'from' => Faker::Name.name,
    'time' => 'Sat Dec 15 22:12:10 UTC 2012'
  }
end





