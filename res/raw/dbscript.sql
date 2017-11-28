DROP TABLE IF EXISTS [launch_info];
DROP TABLE IF EXISTS [goods_cache_info];

CREATE TABLE "launch_info" (
[mission_id] INTEGER NOT NULL, 
[url] TEXT, 
[begin_time] TEXT, 
[end_time] TEXT, 	
CONSTRAINT [] PRIMARY KEY ([mission_id]));
  
CREATE TABLE "goods_cache_info" (
[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
[goods_id] INTEGER NOT NULL, 
[goods_name] TEXT, 
[current_price] TEXT, 
[original_price] TEXT, 	
[discount] TEXT, 
[sales] TEXT, 
[postage] INTEGER DEFAULT (0), 
[pic_url] TEXT, 
[detail_url] TEXT, 
[description] TEXT,
[user_sign] TEXT,
[cache_type] INTEGER NOT NULL);

CREATE TABLE "history_info" (
[_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
[name] TEXT);
  
  
