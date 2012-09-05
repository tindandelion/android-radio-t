require "pathname"
require "pry"

RES_DIR = Pathname(__FILE__).dirname.join('Radio-T', 'res')
MDPI_DIR_PREFIX = 'drawable-mdpi'
XHDPI_DIR_PREFIX = 'drawable-xhdpi'

def mdpi_directories
  RES_DIR.children.select do |path|
    path.directory? &&
      path.basename.to_s.start_with?(MDPI_DIR_PREFIX)
  end
end

