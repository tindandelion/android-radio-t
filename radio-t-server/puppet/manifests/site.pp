group { "puppet":
  ensure => "present",
}

exec { "apt-update":
    command => "/usr/bin/apt-get update",
    require => Group[puppet]
}

Exec["apt-update"] -> Package<||>

include openfire
include radio-t-server