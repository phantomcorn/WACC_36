#!/usr/bin/ruby
# Ruby front-end for your compiler.
# You are free to change the language used for this script,
# but do *not* change its name.

system("kotlin -cp bin:lib/antlr-4.9.3-complete.jar MainKt #{ARGV[0]}")
exit $?.exitstatus
