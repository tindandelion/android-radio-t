require_relative 'drawables_common'

DEFAULT_DENSITY_MAP = {
  ldpi: 67.5,
  mdpi: 90,
  hdpi: 135,
  xhdpi: 180
}

def create_button_drawables(svg_file_path, resource_dir)
  each_density DEFAULT_DENSITY_MAP, resource_dir do |dest_dir, dpi|
    Inkscape.export svg_file_path, png_path_from(svg_file_path, dest_dir), dpi
  end
end
