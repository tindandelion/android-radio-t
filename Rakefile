require 'pathname'

WORK_DIR = Pathname(__FILE__).dirname
require WORK_DIR + 'tools' + 'drawables'

SVG_DIR = WORK_DIR + 'artwork'
RESOURCE_DIR = WORK_DIR + 'Radio-T' + 'res'

desc "Print all missing drawables"
task :find_missing do
  missing = find_missing_drawables(SVG_DIR, RESOURCE_DIR)
  missing.each { |ea| puts ea }
end
