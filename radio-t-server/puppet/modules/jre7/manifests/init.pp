class jre7 {
  package { "openjdk-7-jre-headless":
    ensure => installed
  }

  exec { "update-default-jre":
      command => "update-alternatives --set java /usr/lib/jvm/java-7-openjdk-i386/jre/bin/java",
      path => $path,
      require => Package["openjdk-7-jre-headless"]
  }
}