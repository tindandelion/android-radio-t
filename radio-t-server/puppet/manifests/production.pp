group { "puppet":
  ensure => "present",
}

exec { "apt-update":
    command => "/usr/bin/apt-get update",
    require => Group[puppet]
}

Exec["apt-update"] -> Package<||>

class { "radio-t-server":
  xmpp_server => 'jabber.ru',
  xmpp_room => 'online@conference.radio-t.com',
  xmpp_username => '<USERNAME_HERE>',
  xmpp_password => '<PASSWORD_HERE>'
}
