module Inkscape
  extend self
  
  INKSCAPE_PATH = "/Applications/Inkscape.app/Contents/Resources/bin/inkscape"

  def invoke(args)
    system "#{INKSCAPE_PATH} #{args}"
  end

  def export(svg_path, png_path, dpi)
    Inkscape.invoke "--file #{svg_path} --export-png #{png_path} --export-dpi #{dpi}"
  end
end

def each_density(density_map, res_dir, &block)
  density_map.each_pair do |density, dpi|
    dest_dir = res_dir + ('drawable' + '-' + density.to_s)
    block.call(dest_dir, dpi)
  end
end

def png_path_from(svg_path, dest_dir)
  base_name = svg_path.basename(".*")
  dest_png = dest_dir + "#{base_name}.png"
end

