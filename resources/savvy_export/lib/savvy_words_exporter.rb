require 'bundler/setup'
require 'mongo'
require 'json'

class SavvyWordsExporter
  
  def initialize(mongo)
    @mongo = mongo
  end
  
  def all_as_json(collection)
    everything = @mongo[collection].find({}).collect{|doc| doc}
    {:words => everything}.to_json
  end
  
end

# be ruby lib/savvy_words_exporter.rb username password all_export.json
if __FILE__ == $0
  
  if ARGV.size < 2
    raise Exception.new("pass in username, password, and filename as arguments")
  end
  
  mongodb = Mongo::Connection.new('flame.mongohq.com', 27036).db('metrics')
  mongodb.authenticate(ARGV[0], ARGV[1])
  
  savvy_export = SavvyWordsExporter.new(mongodb)
  all_json = savvy_export.all_as_json :words
  File.open(ARGV[2],'w'){ |f| f << all_json }
end