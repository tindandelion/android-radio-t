class openfire {
  exec { "download_openfire":
    command => "wget -O /tmp/openfire_3.9.1_all.deb http://www.igniterealtime.org/downloadServlet?filename=openfire/openfire_3.9.1_all.deb",
    path => $path
  }

  package { "default-jre-headless":
      ensure => present
  }

  package { "openfire":
      provider => dpkg,
      source => "/tmp/openfire_3.9.1_all.deb",
      require => [Exec["download_openfire"], Package["default-jre-headless"]]
  }
}

