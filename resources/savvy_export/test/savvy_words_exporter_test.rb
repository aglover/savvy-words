require 'bundler/setup'
require 'test/unit'
require 'shoulda'
require 'mongo'
require 'mocha'
require File.expand_path(File.dirname(__FILE__) + '/../lib/savvy_words_exporter')

class SavvyWordsExporterTest < Test::Unit::TestCase

  context 'json documents imported into file' do
    
    setup do
      @mongodb = Mongo::Connection.new('localhost').db('local_development')
      @mongodb[:words].insert({:spelling => 'acrid', :synonyms => [], 
          :definitions => [{
            :definition => 'Having an irritatingly strong and unpleasant taste or smell. Angry and bitter',
            :part_of_speech => 'adjective',
            :example_sentence => 'An acrid odor filled the room.' }]})
            
      @mongodb[:words].insert({
          :spelling => 'bellicose',
          :definitions => [{ :definition =>  'inclined or eager to fight; aggressively hostile; belligerent; pugnacious.', :part_of_speech => 'adjective'}]})
    end
 
    should 'create a hash of words' do
       savvy_exporter = SavvyWordsExporter.new(@mongodb)     
       json_doc = savvy_exporter.all_as_json(:words)
       assert_not_nil json_doc
       json = JSON.parse(json_doc)
       assert_not_nil json['words']
       all_words = json['words']
       assert_equal 2, all_words.size
    end
    
    teardown do
      @mongodb[:words].drop
    end
    
  end
  
end