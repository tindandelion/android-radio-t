require_relative 'drawables_common'

DEFAULT_DENSITY_MAP = DensityMap.default(90)

def create_button_drawables(svg_file_path, resource_dir)
  DEFAULT_DENSITY_MAP.each_density(resource_dir) do |dest_dir, dpi|
    Inkscape.export svg_file_path, png_path_from(svg_file_path, dest_dir), dpi
  end
end
