require 'rexml/document'
require 'pry'

require_relative 'drawables_common'

HOME_BTN_DENSITY_MAP = DensityMap.default(60)

DEFAULT_LAYERS = ['Main', 'Background']
BUTTON_STATES = {
  default: DEFAULT_LAYERS,
  pressed: DEFAULT_LAYERS + ['Pressed', 'Selected'],
  selected: DEFAULT_LAYERS + ['Selected']
}

def create_home_screen_buttons(src_dir, resource_dir)
  HOME_BTN_DENSITY_MAP.each_density resource_dir do |dest_dir, dpi|
    Dir.glob(src_dir + "*.svg") do |svg_file|
      create_home_screen_button(Pathname(svg_file), dest_dir, dpi)
    end
  end    
end

def create_home_screen_button(svg_source, dest_dir, dpi)
  BUTTON_STATES.keys.each do |state|
    base_name = svg_source.basename(".*")
    dest_png = dest_dir + "#{base_name}_#{state}.png"
    export_png(svg_source, dest_png, dpi, BUTTON_STATES[state])
  end
end

def export_png(src_svg, dest_png, dpi, visible_layers)
  transform_file(src_svg) do |temp_svg|
    setup_visible_layers(temp_svg, visible_layers)
    Inkscape.export(temp_svg, dest_png, dpi)
  end
end

def transform_file(orig_path, &block)
  tmp_path = Pathname.getwd + 'tempfile.svg'
  FileUtils.cp orig_path, tmp_path
  block.call(tmp_path)
ensure
  tmp_path.delete if tmp_path.exist?
end

class SvgImage
  class Layer
    attr_reader :el
    def initialize(el)
      @el = el
    end

    def name
      @el.attribute('inkscape:label').value
    end

    
  end
      
  def self.open(path)
    root = REXML::Document.new(path.read)
    self.new(root)
  end

  def initialize(root)
    @root = root
  end

  def layers
    els = REXML::XPath.match(@root, "//g[@inkscape:groupmode='layer']")
    els.collect { |el| Layer.new(el) }
  end

  def write(path)
    path.open('w') { |io| @root.write(io) }
  end
end

def setup_visible_layers(svg_path, visible_layers)
  svg = SvgImage.open(svg_path)
  svg.layers.each do |l|
    style_value = if visible_layers.include?(l.name)
                    "display:inline"
                  else
                    "display:none"
                  end
    l.el.add_attribute('style', style_value)
  end
  svg.write(svg_path)
end
