require 'pathname'
require 'tmpdir'
require 'pry'

require Pathname(__FILE__).dirname + 'drawables.rb'

describe 'Missing drawables' do
  let(:work_dir) { Pathname(Dir.mktmpdir) }
  let(:svg_dir) { work_dir + 'artwork' }
  let(:drawables_dir) { work_dir + 'drawables' }
  
  before :each do 
    svg_dir.mkpath
    drawables_dir.mkpath
  end
  
  after(:each) { FileUtils.remove_entry_secure work_dir }

  it 'returns drawables for all densities' do
    create_file svg_dir + 'icon.svg'
    
    missing_drawables = find_missing_drawables(svg_dir, drawables_dir)
    missing_drawables.should include(drawables_dir.join('drawable-ldpi', 'icon.png'),
                                     drawables_dir.join('drawable-mdpi', 'icon.png'),
                                     drawables_dir.join('drawable-hdpi', 'icon.png'),
                                     drawables_dir.join('drawable-xhdpi', 'icon.png'))
  end

  it 'excludes existing drawable files' do
    existing_drawable = drawables_dir.join('drawable-mdpi', 'icon.png')
    
    create_file svg_dir + 'icon.svg'
    create_file existing_drawable

    missing_drawables = find_missing_drawables(svg_dir, drawables_dir)
    missing_drawables.should_not include(existing_drawable)
  end

  it 'skips the sub-directories which are not version-specific dirs' do
    FileUtils.mkpath svg_dir + 'subdir'

    missing_drawables = find_missing_drawables(svg_dir, drawables_dir)
    missing_drawables.should be_empty
  end

  it 'processes version specific directories' do
    ics_svgs = svg_dir + 'v15'
    create_file ics_svgs + 'icon.svg'

    missing_drawables = find_missing_drawables(svg_dir, drawables_dir)
    missing_drawables.should include(drawables_dir.join('drawable-ldpi-v15', 'icon.png'),
                                     drawables_dir.join('drawable-mdpi-v15', 'icon.png'),
                                     drawables_dir.join('drawable-hdpi-v15', 'icon.png'),
                                     drawables_dir.join('drawable-xhdpi-v15', 'icon.png'))
  end

  def create_file(path)
    FileUtils.mkpath path.dirname
    FileUtils.touch path
  end
end

