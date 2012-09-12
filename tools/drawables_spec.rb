require 'pathname'
require 'tmpdir'
require 'pry'

DENSITIES = %w(ldpi mdpi hdpi xhdpi)

def find_missing_drawables(svg_dir, drawables_dir)
  missing_files = []
  svg_dir.children.each do |svg|
    name = svg.basename('.*').to_s
    DENSITIES.collect do |d|
      png_file = drawables_dir + ('drawable-' + d) + (name + '.png')
      missing_files << png_file unless png_file.exist?
    end
  end
  missing_files
end

describe 'Missing drawables' do
  include FileUtils
  
  let(:work_dir) { Pathname(Dir.mktmpdir) }
  let(:svg_dir) { work_dir + 'artwork' }
  let(:drawables_dir) { work_dir + 'drawables' }
  
  before :each do 
    svg_dir.mkpath
    drawables_dir.mkpath
  end
  
  after(:each) { FileUtils.remove_entry_secure work_dir }

  it 'returns drawables for all densities' do
    touch svg_dir + 'icon.svg'
    
    missing_drawables = find_missing_drawables(svg_dir, drawables_dir)
    missing_drawables.should include(drawables_dir.join('drawable-mdpi', 'icon.png'),
                                     drawables_dir.join('drawable-hdpi', 'icon.png'),
                                     drawables_dir.join('drawable-ldpi', 'icon.png'),
                                     drawables_dir.join('drawable-xhdpi', 'icon.png'))
  end

  it 'excludes existing drawable files' do
    existing_drawable = drawables_dir.join('drawable-mdpi', 'icon.png')
    
    touch svg_dir + 'icon.svg'
    
    mkpath existing_drawable.dirname
    touch existing_drawable

    missing_drawables = find_missing_drawables(svg_dir, drawables_dir)
    missing_drawables.should_not include(existing_drawable)
  end
end

