class radio-t-server {
  $jre_home = "/usr/lib/jvm/java-7-openjdk-amd64/jre"

  $xmpp_server = "localhost"

  package { "openjdk-7-jre-headless":
    ensure => installed
  }

  package { "jsvc":
      ensure => installed
  }

  exec { "update-default-jre":
      command => "update-alternatives --set java ${jre_home}/bin/java",
      path => $path,
      require => Package["openjdk-7-jre-headless"]
  }

  file { "/var/log/radio-t-server":
      ensure => directory
  }

  file { "/opt/radio-t-server":
      ensure => directory
  }

  file { "/etc/init.d/radio-t-server":
      content => template("radio-t-server/start-stop-script.erb"),
      mode => "a+x"
  }

  file { "/etc/radio-t-server.conf":
      content => template("radio-t-server/radio-t-server.conf.erb")
  }
}