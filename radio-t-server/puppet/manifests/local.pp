group { "puppet":
  ensure => "present",
}

exec { "apt-update":
    command => "/usr/bin/apt-get update",
    require => Group[puppet]
}

Exec["apt-update"] -> Package<||>

class { "openfire": }

class { "radio-t-server":
  xmpp_server => 'localhost',
  xmpp_room => 'online@conference.precise64',
  xmpp_username => 'android-radiot',
  xmpp_password => 'password'
}