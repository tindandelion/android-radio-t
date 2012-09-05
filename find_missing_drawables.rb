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

def version_suffix_from(path)
  components = path.basename.to_s.split('-')
  return "" if components.size < 3
  return '-' + components.last
end

def xhdpi_directory(version_suffix)
  RES_DIR.join(XHDPI_DIR_PREFIX + version_suffix)
end

mdpi_directories.each do |path|
  version_suffix = version_suffix_from(path)
  xhdpi_dir = xhdpi_directory(version_suffix)
  puts xhdpi_dir
  path.children.each do |file|
    resource_name = file.basename.to_s
    xhdpi_file = xhdpi_dir + resource_name
    puts "\t" + resource_name unless xhdpi_file.exist?
  end
end
