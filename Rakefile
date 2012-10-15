
require 'pathname'

WORK_DIR = Pathname(__FILE__).dirname
require WORK_DIR + 'tools' + 'drawables'
require WORK_DIR + 'tools' + 'home_screen_buttons'

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

desc "Run audio server"
task :audio_server do
  ruby "test-server/server.rb"
end

