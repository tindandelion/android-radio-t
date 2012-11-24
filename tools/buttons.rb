require_relative 'drawables_common'

DEFAULT_DENSITY_MAP = DensityMap.default(90)

def create_button_drawables(svg_path, resource_dir)
  DEFAULT_DENSITY_MAP.each_density(resource_dir) do |dest_dir, dpi|
    export_button_drawables svg_path, dest_dir, dpi
  end
end

def export_button_drawables(svg_path, dest_dir, dpi)
  Inkscape.export svg_path, png_path_from(svg_path, dest_dir), dpi
end

def png_path_from(svg_path, dest_dir)
  base_name = svg_path.basename(".*")
  dest_png = dest_dir + "#{base_name}_default.png"
end
