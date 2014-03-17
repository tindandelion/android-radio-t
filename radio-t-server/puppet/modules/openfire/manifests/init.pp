class openfire {
  exec { "download_openfire":
      command => "wget -O /tmp/openfire_3.9.1_all.deb http://www.igniterealtime.org/downloadServlet?filename=openfire/openfire_3.9.1_all.deb",
      path => $path,
      unless => "test -e /tmp/openfire_3.9.1_all.deb"
  }

  package { "default-jre-headless":
      ensure => present
  }

  package { "openfire":
      provider => dpkg,
      source => "/tmp/openfire_3.9.1_all.deb",
      require => [Exec["download_openfire"], Package["default-jre-headless"]]
  }

  service { "openfire":
    ensure => running,
    require => Package["openfire"]
  }

  file { "openfire_config":
      path => "/etc/openfire",
      source => "puppet:///modules/openfire/etc/openfire",
      recurse => true,
      owner => "openfire",
      group => "openfire",
      require => Package["openfire"]
  }

  file { "openfire_db":
    path => "/var/lib/openfire/embedded-db",
    source => "puppet:///modules/openfire/embedded-db",
    recurse => true,
    owner => "openfire",
    group => "openfire",
    require => [Package["openfire"], File["openfire_config"]],
    notify => Service["openfire"]
  }
}

