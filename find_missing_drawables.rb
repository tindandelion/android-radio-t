require "pathname"
require "pry"

PROJECT_PATH = Pathname('Radio-T')
RES_DIR = PROJECT_PATH + 'res'

class Resources
  MDPI_DIR_PREFIX = 'drawable-mdpi'
  XHDPI_DIR_PREFIX = 'drawable-xhdpi'
  
  def initialize(project_path)
    @res_path = project_path + 'res'
  end

  def mdpi_directories
    @res_path.children.select do |path|
      path.directory? &&
        path.basename.to_s.start_with?(MDPI_DIR_PREFIX)
    end
  end

  def xhdpi_directory(version_suffix)
    @res_path.join(XHDPI_DIR_PREFIX + version_suffix)    
  end
end

$resources = Resources.new(PROJECT_PATH)

def version_suffix_from(path)
  components = path.basename.to_s.split('-')
  return "" if components.size < 3
  return '-' + components.last
end

def xhdpi_directory(version_suffix)
  $resources.xhdpi_directory(version_suffix)
end

def missing_drawables_xhdpi
  result = []
  $resources.mdpi_directories.each do |mdpi_path|
    version_suffix = version_suffix_from(mdpi_path)
    xhdpi_dir = xhdpi_directory(version_suffix)
    mdpi_path.children.each do |file|
      resource_name = file.basename.to_s
      xhdpi_path = xhdpi_dir + resource_name
      result << xhdpi_path unless xhdpi_path.exist?
    end
  end
  result
end

def missing_artwork_files
  result = []
  $resources.mdpi_directories.each do |mdpi_path|
    version_suffix = version_suffix_from(mdpi_path)
    puts version_suffix
  end
  result
end  

# puts missing_drawables_xhdpi  
puts missing_artwork_files
