class timezone($zone) {
  file { "/etc/timezone":
    ensure => present,
    content => $zone
  }

  exec { "reconfigure-tzdata":
    command => "/usr/sbin/dpkg-reconfigure --frontend noninteractive tzdata"
  }

  File["/etc/timezone"] -> Exec["reconfigure-tzdata"]
}
