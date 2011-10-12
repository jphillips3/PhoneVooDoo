
###############################################################################
# Need ruby libs:
###############################################################################
require 'rubygems'
require 'libxml'
require 'getoptlong'

###############################################################################
# PrintHelp -- function
#   Prints the help message for this script.
#
# Input:
#   None.
#
# Output:
#   None.
#
###############################################################################
def PrintHelp()
  print "Some help message goes here...\n\n"
end

###############################################################################
# Main -- function
#   This is a C like main function to allow for eaiser debugging and 
#   reading of flow control for this script.
#
# Input:
#   None.
#
# Output:
#   Always returns 0.
#
###############################################################################
def Main
  opts = nil
  help = false
  
  begin
    opts = GetoptLong.new(
      ["--help", '-h', GetoptLong::NO_ARGUMENT],
      ["--phone", '-p', GetoptLong::REQUIRED_ARGUMENT],
      ["--test", '-t', GetoptLong::OPTIONAL_ARGUMENT]
    )
    
    opts.quiet = true
    opts.each do |opt, arg|
      case (opt)
        when "--help"
          help = true
      end
    end
  rescue Exception => e
    print "Error: '#{e.message}'!\n"
    exit(1)
  end
  
  if (help)
    
  end
  
  
  return 0
end

###############################################################################
# Start executing code here -->
###############################################################################
  Main()
  exit(0)
