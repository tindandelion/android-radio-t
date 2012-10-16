class RandomDelayProfile
  DELAY_VARIATION = 0.05
  SECOND_DELAY_CHANCE = 0.01
  GRAND_DELAY_CHANCE = 0.001
  GRAND_DELAY = 40

  def initialize(mean_value)
    @mean_value = mean_value
  end
  
  def introduce_delay
    sleep randomize(@mean_value)
  end

  private

  def randomize(median)
    fluctuated = median + fluctuation + second_delay + grand_delay
  end

  def second_delay
    chances = rand()
    (chances < SECOND_DELAY_CHANCE) ? 1 : 0
  end

  def grand_delay
    chances = rand()
    (chances < GRAND_DELAY_CHANCE) ? GRAND_DELAY : 0
  end

  def fluctuation
    ((DELAY_VARIATION / 2) - (DELAY_VARIATION * rand()))
  end
end

class ConstantDelayProfile
  def initialize(speed_factor)
    @speed_factor = speed_factor
  end

  def introduce_delay
    sleep @speed_factor
  end
end

