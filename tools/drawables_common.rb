module Inkscape
  extend self
  
  INKSCAPE_PATH = "/Applications/Inkscape.app/Contents/Resources/bin/inkscape"

  def invoke(args)
    system "#{INKSCAPE_PATH} #{args}"
  end

  def export_image(svg_image, png_path, dpi)
    tempfile = Pathname("tempfile.svg")
    begin
      svg_image.write tempfile
      export tempfile, png_path, dpi
    ensure
      tempfile.delete
    end
  end

  def export(svg_path, png_path, dpi)
    Inkscape.invoke "--file #{svg_path} --export-png #{png_path} --export-dpi #{dpi}"
  end
end

class DensityMap
  def self.default(mdpi_res)
    self.new(ldpi: mdpi_res * 120 / 160,
             mdpi: mdpi_res,
             hdpi: mdpi_res * 240 / 160,
             xhdpi: mdpi_res * 320 / 160)
  end
  
  def initialize(denisty_resolution_map = {})
    @map = denisty_resolution_map
  end

  def each_density(resources_dir, &block)
    @map.each_pair do |density, dpi|
      dest_dir = drawable_dir_for_density(resources_dir, density)
      block.call(dest_dir, dpi)
    end
  end

  def drawable_dir_for_density(basedir, density)
    basedir + ('drawable' + '-' + density.to_s)
  end
end

class SvgImage
  class Layer
    def initialize(el)
      @el = el
    end

    def name
      @el.attribute('inkscape:label').value
    end

    def show
      set_style "display:inline"
    end

    def hide
      set_style "display:none"
    end

    private
    
    def set_style(value)
      @el.add_attribute('style', value)
    end
  end
      
  def self.open(path)
    root = REXML::Document.new(path.read)
    self.new(root)
  end

  def self.transform_file(path, &block)
    svg = open(path)
    block.call(svg)
    svg.write(path)
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

class ImageLayout
  def self.with_layers(*layers)
    self.new(layers)
  end
  
  def initialize(layers)
    @layers = layers
  end

  def add_layers(*more_layers)
    self.class.new(@layers + more_layers)
  end

  def apply_to(svg_image)
    svg_image.layers.each do |l|
      if visible?(l.name)
        l.show
      else
        l.hide
      end
    end
  end

  def visible?(name)
    @layers.include?(name)
  end
end

def with_tempfile(orig_path, &block)
  tmp_path = Pathname.getwd + 'tempfile.svg'
  FileUtils.cp orig_path, tmp_path
  block.call(tmp_path)
ensure
  tmp_path.delete if tmp_path.exist?
end

