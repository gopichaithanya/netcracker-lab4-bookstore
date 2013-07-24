DROP database IF EXISTS bookstore;

CREATE database bookstore;
USE bookstore;
	CREATE TABLE authors (	authorId int NOT NULL auto_increment, 
							firstName varchar(80) NOT NULL, 
							lastName varchar(80) NOT NULL,
							CONSTRAINT authors_pk PRIMARY KEY (authorId),
							CONSTRAINT authors_flname_uk UNIQUE KEY (firstName, lastName)
						 );
							
	CREATE TABLE publishers (	publishId int NOT NULL auto_increment,
								publishName varchar(80) NOT NULL,
								publishUrl varchar(100),
								CONSTRAINT publishers_pk PRIMARY KEY (publishId)
							);
							
	CREATE TABLE genres (	genreId INT NOT NULL auto_increment,  
							name VARCHAR(25) NOT NULL,
							CONSTRAINT genreId_pk PRIMARY KEY (genreId),
							CONSTRAINT genreName_uk UNIQUE KEY (name)
						);		
						
	CREATE TABLE books (	booksId int NOT NULL auto_increment,
							title varchar(70) NOT NULL,
							publishId int NOT NULL,
							genreId int NOT NULL,
							descript varchar(1000),
							imgref varchar(70),
							year int NOT NULL,
							CONSTRAINT books_id_pk PRIMARY KEY (booksId),
							CONSTRAINT books_publish_id_fk FOREIGN KEY (publishId) REFERENCES publishers (publishId),
							CONSTRAINT books_genreId_fk FOREIGN KEY (genreId) REFERENCES genres (genreId),
							CONSTRAINT books_title_uk UNIQUE KEY (title)
						);
						
	CREATE TABLE balink	(	authorId int NOT NULL,
							bookId int NOT NULL,							
							CONSTRAINT PRIMARY KEY (authorId, bookId),		
							CONSTRAINT balink_author_id_fk FOREIGN KEY (authorId) REFERENCES authors (authorId),
							CONSTRAINT balink_book_id_fk FOREIGN KEY (bookId) REFERENCES books (booksId)	
						);
						
	CREATE TABLE users 	(	id int NOT NULL auto_increment,
							nick varchar(50) NOT NULL,
							password varchar(80) NOT NULL,
							status varchar(50) NOT NULL,
							CONSTRAINT usersId_pk PRIMARY KEY (id),
							CONSTRAINT users_uk UNIQUE KEY (nick)
						);					
						
	/*DELETE FROM authors;*/
	INSERT INTO authors VALUES	(1303, 'David', 'Sedaris'),
								(1304, 'Sheryl', 'Sandberg'),
								(1305, 'Scott', 'Fitzgerald'),
								(1306, 'Bob', 'Harper'),
								(1307, 'Kelly', 'Starrett'),
								(1308, 'Michael', 'Pollan'),
								(1309, 'Jon', 'Duckett'),
								(1310, 'George', 'Martin'),
								(1311, 'Gary', 'Keller'),
								(1312, 'Dan', 'Brown'),
								(1313, 'Ebeh', 'Alexander'),
								(1314, 'Gwyneth', 'Paltrow'),
								(1315, 'Sarah', 'Young'),
								(1316, 'Tom', 'Rath'),
								(1317, 'Charlaine', 'Harris'),
								(1318, 'Nick', 'Tate');
	/*DELETE FROM publishers;	

	DELETE FROM genres;*/
	INSERT INTO genres VALUES	(2345, 'Other'),
								(2346, 'Science'),
								(2347, 'Novel'),
								(2348, 'Cookery'),
								(2349, 'Psychology'),
								(2350, 'Politics'),
								(2351, 'History'),
								(2352, 'Religion'),
								(2353, 'Fitnes'),
								(2354, 'Progrmming'),
								(2355, 'Fantasy');
								
 	INSERT INTO publishers VALUES 	(123, 'Little, Brown and Company', 'http://www.amazon.com/Lets-Explore-Diabetes-David-Sedaris/dp/0316154695/'),
									(124, 'Knopf', 'http://www.amazon.com/Lean-In-Women-Work-Will/dp/0385349947/'),
									(125, 'Scribner', 'http://www.amazon.com/The-Great-Gatsby-Scott-Fitzgerald/dp/0743273567/'),
									(126, 'Ballantine Books', 'http://www.amazon.com/Jumpstart-Skinny-Simple-3-Week-Supercharged/dp/0345545109/'),
									(127, 'Victory Belt Publishing', 'http://www.amazon.com/Becoming-Supple-Leopard-Preventing-Performance/dp/1936608588/'),
									(128, 'Penguin Press HC', 'http://www.amazon.com/Cooked-A-Natural-History-Transformation/dp/1594204217/'),
									(129, 'Wiley', 'http://www.amazon.com/HTML-CSS-Design-Build-Websites/dp/1118008189/'),
									(130, 'Bantam', 'http://www.amazon.com/Books-Thrones-Feast-Crows-Swords/dp/0345529057/'),
									(131, 'Bard Pres', 'http://www.amazon.com/The-ONE-Thing-Surprisingly-Extraordinary/dp/1885167776/'),
									(132, 'Doubleday', 'http://www.amazon.com/Inferno-A-Novel-Robert-Langdon/dp/0385537859/'),
									(133, 'Simon & Schuster', 'http://www.amazon.com/Proof-Heaven-Neurosurgeons-Journey-Afterlife/dp/1451695195/'),
									(134, 'Grand Central Life & Style', 'http://www.amazon.com/Its-All-Good-Delicious-Recipes/dp/1455522716/'),
									(135, 'Thomas Nelson', 'http://www.amazon.com/Jesus-Calling-Enjoying-Peace-Presence/dp/1591451884/'),
									(136, 'Gallup Press', 'http://www.amazon.com/StrengthsFinder-2-0-Tom-Rath/dp/159562015X/'),
									(137, 'Ace Hardcover', 'http://www.amazon.com/Dead-Ever-After-Sookie-Stackhouse/dp/193700788X/'),
									(138, 'Humanix Books', 'http://www.amazon.com/ObamaCare-Survival-Guide-Nick-Tate/dp/0893348627/');
	/*DELETE FROM books;*/
	INSERT INTO books VALUES	(95, 'Let\'s Explore Diabetes with Owls', 123, 2345, 'Although his self-deprecating stories are most effective at pointing out the absurdities in everyday life, 
											and sometimes share similar formulas, it\'s a formula I find never gets old. To me, his writing is akin to curling up on the couch with your family in front 
											of a large fire telling stories.', '95.jpg', 2012),
								(96, 'Lean In: Women, Work, and the Will to Lead', 124, 2345, ' Anyone who\'s watched Sheryl Sandberg\'s popular TED Talk, "Why We Have Too Few Women Leaders," 
											is familiar with--and possibly haunted by--the idea of "having it all." "Perhaps the greatest trap ever set for women 
											was the coining of this phrase," writes Sandberg in Lean In, which expands on her talk\'s big idea: that increasing 
											the number of women at the top of their fields will benefit everyone.', '96.jpg' ,2012),
								(97, 'The Great Gatsby', 125, 2345, 'In 1922, F. Scott Fitzgerald announced his 
											decision to write "something new--something extraordinary and beautiful 
											and simple + intricately patterned." That extraordinary, beautiful, intricately patterned, 
											and above all, simple novel became The Great Gatsby, arguably Fitzgerald\'s finest work and 
											certainly the book for which he is best known.', '97.jpg', 2012),
								(98, 'Jumpstart to Skinny: The Simple 3-Week Plan', 126, 2353, 'Bob Harper is a world-renowned fitness trainer and the longest-reigning 
											star of the NBC reality series The Biggest Loser, which went into its fourteenth season in January 2013. He has released several popular fitness DVDs and is the 
											author of the #1 New York Times bestseller The Skinny Rules. Harper still teaches a local spin class in Los Angeles, where he resides with his dog, Karl.', '98.jpg', 2013),
								(99, 'Becoming a Supple Leopard', 127, 2353, 'Improve your athletic performance, extend your athletic career, treat body stiffness and achy joints, and rehabilitate 
											injuries?all without having to seek out a coach, doctor, chiropractor, physical therapist, or masseur.', '99.jpg', 2013),
								(100, 'Cooked: A Natural History of Transformation', 128, 2348, 'In Cooked, Michael Pollan explores the previously uncharted territory of his own kitchen. Here, he 
											discovers the enduring power of the four classical elements?fire, water, air, and earth?to transform the stuff of nature into delicious things to eat and drink. 
											Apprenticing himself to a succession of culinary masters, Pollan learns how to grill with fire, cook with liquid, bake bread, and ferment everything from cheese to beer.', '100.jpg', 2012),
								(101, 'HTML and CSS: Design and Build Websites', 129, 2354, 'Every day, more and more people want to learn some HTML and CSS. Joining the professional web designers and programmers 
											are new audiences who need to know a little bit of code at work (update a content management system or e-commerce store) and those who want to make their personal blogs more attractive. 
											Many books teaching HTML and CSS are dry and only written for those who want to become programmers, which is why this book takes an entirely new approach.', '101.jpg', 2012),
								(102, 'A Song of Ice and Fire', 130, 2355, 'George R. R. Martin\'s A Song of Ice and Fire series has become, in many ways, the gold standard for modern epic fantasy. 
											Martin--dubbed the "American Tolkien" by Time magazine--has created a world that is as rich and vital as any piece of historical fiction, set in an age of knights and chivalry 
											and filled with a plethora of fascinating, multidimensional characters that you love, hate to love, or love to hate as they struggle for control of a divided kingdom. It is this 
											very vitality that has led it to be adapted as the HBO miniseries "Game of Thrones."', '102.jpg', 2012),
								(103, 'The Surprisingly Simple Truth Behind Extraordinary Results', 131, 2345, 'You want fewer distractions and less on your plate. The daily barrage of e-mails, texts, tweets, messages, 
											and meetings distract you and stress you out. The simultaneous demands of work and family are taking a toll. And what\?s the cost? Second-rate work, missed deadlines, smaller paychecks, fewer promotions?and lots of stress.', '103.jpg', 2013),
								(104, 'Inferno: A Novel ', 132, 2347, 'In the heart of Italy, Harvard professor of symbology, Robert Langdon, is drawn into a harrowing world centered on one of history\?s most enduring and 
											mysterious literary masterpieces . . . Dante\?s Inferno.', '104.jpg', 2012),
								(105, 'Proof of Heaven: A Neurosurgeon\'s Journey into the Afterlife', 133, 2346, 'Thousands of people have had near-death experiences, but scientists have argued that they are impossible. Dr. Eben Alexander was one of those scientists. 
											A highly trained neurosurgeon, Alexander knew that NDEs feel real, but are simply fantasies produced by brains under extreme stress.', '105.jpg', 2012),
								(106, 'Delicious, Easy Recipes That Will Make You Look Good and Feel Great', 134, 2348, 'Gwyneth Paltrow, Academy-Award winning actress and bestselling cookbook author, returns with recipes 
											for the foods she eats when she wants to lose weight, look good, and feel more energetic.', '106.jpg', 2012),
								(107, 'Jesus Calling: Enjoying Peace in His Presence', 135, 2352, 'Jesus Calling is a devotional filled with uniquely inspired treasures from heaven for every day of the year.  
											After many years of writing in her prayer journal, missionary Sarah Young decided to listen to God with pen in hand, writing down whatever she believed He was saying to her.', '107.jpg', 2012),
								(108, 'StrengthsFinder 2.0', 136, 2349, 'To help people uncover their talents, Gallup introduced the first version of its online assessment, StrengthsFinder, in 2001 which ignited 
											a global conversation and helped millions to discover their top five talents.', '108.jpg', 2012),
								(109, 'Dead Ever After: A Sookie Stackhouse Novel ', 137, 2347, 'Sookie Stackhouse  finds it easy to turn down the request of former barmaid Arlene when she wants her job back at Merlotte\?s. 
											After all, Arlene tried to have Sookie killed. But her relationship with Eric Northman is not so clearcut. He and his vampires are keeping their distance?and a cold silence. 
											And when Sookie learns the reason why, she is devastated.', '109.jpg', 2012),
								(110, 'ObamaCare Survival Guide', 138, 2350, 'Congress passed it. President Obama signed it into law. The Supreme Court has ruled it constitutional. And Obama\'s re-election virtually ensures 
											the Patient Protection and Affordable Care Act, also known as ObamaCare, will be implemented in full, with the prospects of wholesale repeal all but impossible. In fact, many provisions of the 
											new law are already becoming a reality with major new regulations set to start soon. ObamaCare will affect every single American, but few know what exactly the 2,700 page law actually says.', '110.jpg', 2013);	
	/*DELETE FROM balink;*/
	INSERT INTO balink VALUES	(1303, 95),
								(1304, 96),
								(1305, 97),
								(1306, 98),
								(1307, 99),
								(1308, 100),
								(1309, 101),
								(1310, 102),
								(1311, 103),
								(1312, 104),
								(1313, 105),
								(1314, 106),
								(1315, 107),
								(1316, 108),
								(1317, 109),
								(1318, 110);

	/*DELETE FROM users;*/	
	INSERT INTO users VALUES	(4545, 'User1', 'pass1', 'user'),
								(4546, 'User2', 'pass2', 'admin');
								
commit;