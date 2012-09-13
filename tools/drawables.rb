DENSITIES = %w(ldpi mdpi hdpi xhdpi)

def drawable_directories(root_path, version = nil)
  directory_template = 'drawable-%s'
  directory_template += ('-' + version) if version
  DENSITIES.collect { |d| root_path + (directory_template % d) }
end

def traverse_artwork_dir(path, version = nil, &block)
  path.children.each do |child|
    if child.file?
      block.call(child, version)
    else
      dirname = child.basename.to_s
      traverse_artwork_dir(child, dirname, &block) if dirname =~ /v\d+/
    end
  end
end

def find_missing_drawables(svg_dir, resource_dir)
  result = []
  traverse_artwork_dir(svg_dir) do |artwork_file, version|
    result += resource_files_for(artwork_file, resource_dir, version).reject(&:exist?)
  end
  result
end

def resource_files_for(artwork_file, resource_dir, version)
  name = artwork_file.basename('.*').to_s
  drawable_directories(resource_dir, version).collect do |dir|
    dir + (name + '.png')
  end
end  
