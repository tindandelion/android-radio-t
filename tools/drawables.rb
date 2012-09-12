DENSITIES = %w(ldpi mdpi hdpi xhdpi)

def find_missing_drawables(svg_dir, drawables_dir)
  missing_files = []
  svg_dir.children.each do |path|
    next if path.directory?
    
    name = path.basename('.*').to_s
    DENSITIES.collect do |d|
      png_file = drawables_dir + ('drawable-' + d) + (name + '.png')
      missing_files << png_file unless png_file.exist?
    end
  end
  missing_files
end

