
require 'pathname'

WORK_DIR = Pathname(__FILE__).dirname
TOOL_DIR = WORK_DIR + 'tools'
require TOOL_DIR + 'drawables'
require TOOL_DIR + 'home_screen_buttons'
require TOOL_DIR + 'buttons'

SVG_DIR = WORK_DIR + 'artwork'
RESOURCE_DIR = WORK_DIR + 'Radio-T' + 'res'

desc "Print all missing drawables"
task :find_missing do
  missing = find_missing_drawables(SVG_DIR, RESOURCE_DIR)
  missing.each { |ea| puts ea.relative_path_from(RESOURCE_DIR) }
end

desc "Create home screen buttons"
task :home_screen_buttons do
  create_home_screen_buttons(SVG_DIR + 'home_buttons', RESOURCE_DIR)
end

desc "Create button drawables"
task :buttons do
  ['ic_play.svg', 'ic_stop.svg'].each do |fn|
    create_button_drawables(SVG_DIR + fn, RESOURCE_DIR)
  end
end

desc "Export all resources"
task resources: [:buttons, :home_screen_buttons]

desc "Run audio server"
task :audio_server do
  ruby "test-server/audio-server.rb"
end

desc "Run chat server"
task :chat_server do
  ruby "test-server/chat-server.rb"
end


