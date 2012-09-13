DENSITIES = %w(ldpi mdpi hdpi xhdpi)

def find_missing_drawables(svg_dir, drawables_dir)
  missing_files = []
  svg_dir.children.each do |path|
    if path.directory?
      if versioned_drawables_directory?(path)
        missing_files += missing_version_dependent_drawables(path, drawables_dir)
      else
        next
      end
    else
      missing_files += missing_version_independent_drawables(path, drawables_dir)
    end
  end
  missing_files
end

def versioned_drawables_directory?(path)
  path.basename.to_s =~ /v\d+/
end

def missing_version_dependent_drawables(svg_dir, drawables_dir)
  result = []
  version = svg_dir.basename.to_s
  svg_dir.children.each do |path|
    name = path.basename('.*').to_s
    DENSITIES.collect do |d|
      png_file = drawables_dir + ('drawable-' + d + '-' + version) + (name + '.png')
      result << png_file unless png_file.exist?
    end
  end
  result
end

def missing_version_independent_drawables(svg_path, drawables_dir)
  result = []
  name = svg_path.basename('.*').to_s
  DENSITIES.collect do |d|
    png_file = drawables_dir + ('drawable-' + d) + (name + '.png')
    result << png_file unless png_file.exist?
  end
  result
end


