require 'rexml/document'
require 'pry'

require_relative 'drawables_common'

HOME_BTN_DENSITY_MAP = DensityMap.default(60)

DEFAULT_LAYOUT = ImageLayout.with_layers('Main', 'Background')
BUTTON_LAYOUTS = {
  default: DEFAULT_LAYOUT,
  pressed: DEFAULT_LAYOUT.add_layers('Pressed', 'Selected'),
  selected: DEFAULT_LAYOUT.add_layers('Selected')
}


def create_home_screen_buttons(src_dir, resource_dir)
  HOME_BTN_DENSITY_MAP.each_density resource_dir do |dest_dir, dpi|
    Dir.glob(src_dir + "*.svg") do |svg_file|
      create_home_screen_button(Pathname(svg_file), dest_dir, dpi)
    end
  end    
end

def create_home_screen_button(svg_source, dest_dir, dpi)
  BUTTON_LAYOUTS.keys.each do |state|
    base_name = svg_source.basename(".*")
    dest_png = dest_dir + "#{base_name}_#{state}.png"
    export_png(svg_source, dest_png, dpi, BUTTON_LAYOUTS[state]) 
  end
end

def export_png(src_svg, dest_png, dpi, layout)
  with_tempfile(src_svg) do |temp_svg|
    SvgImage.transform_file(temp_svg) do |svg|
      layout.apply_to svg
    end
    Inkscape.export(temp_svg, dest_png, dpi)
  end
end
