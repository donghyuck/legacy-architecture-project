<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="utf-8" />
    <title>Main</title>
    <script src="/includes/scripts/yui/yui-min.js" type="text/javascript" charset="utf-8"></script>
  </head>  
  <body>
  <header></header>
  <section>
  
		<div id="scrollview-container"> 
            <div id="scrollview-header"> 
                <h1>Basic ScrollView</h1> 
            </div> 
            <div id="scrollview-content" class="yui3-scrollview-loading"> 
                <ul> 
                    <li>AC/DC</li> 
                    <li>Aerosmith</li> 
                    <li>Billy Joel</li> 
                    <li>Bob Dylan</li> 
                    <li>Bob Seger</li> 
                    <li>Bon Jovi</li> 
                    <li>Bruce Springsteen</li> 
                    <li>Creed</li> 
                    <li>Creedence Clearwater Revival</li> 
                    <li>Dave Matthews Band</li> 
                    <li>Def Leppard</li> 
                    <li>Eagles</li> 
                    <li>Eminem</li> 
                    <li>Fleetwood Mac</li> 
                    <li>Green Day</li> 
                    <li>Guns N' Roses</li> 
                    <li>James Taylor</li> 
                    <li>Jay-Z</li> 
                    <li>Jimi Hendrix</li> 
                    <li>Led Zeppelin</li> 
                    <li>Lynyrd Skynyrd</li> 
                    <li>Metallica</li> 
                    <li>Motley Crue</li> 
                    <li>Neil Diamond</li> 
                    <li>Nirvana</li> 
                    <li>Ozzy Osbourne</li> 
                    <li>Pearl Jam</li> 
                    <li>Pink Floyd</li> 
                    <li>Queen</li> 
                    <li>Rod Stewart</li> 
                    <li>Rush</li> 
                    <li>Santana</li> 
                    <li>Simon and Garfunkel</li> 
                    <li>Steve Miller Band</li> 
                    <li>The Beatles</li> 
                    <li>The Doors</li> 
                    <li>The Police</li> 
                    <li>The Rolling Stones</li> 
                    <li>Tom Petty</li> 
                    <li>U2</li> 
                    <li>Van Halen</li> 
                    <li>Willie Nelson</li> 
                    <li>ZZ Top</li> 
                </ul> 
            </div> 
        </div>   
  
  </section>
    <script type="text/javascript" charset="utf-8"> 
 
        YUI().use('scrollview-base', function(Y) {
            var scrollView = new Y.ScrollView({
                    id:"scrollview",
                    srcNode: '#scrollview-content',
                    height: 310,
                    flick: {
                        minDistance:10,
                        minVelocity:0.3,
                        axis: "y"
                    }
            });
 
            scrollView.render();
        });
 
    </script> 
        
  <tailer></tailer>  
</body>
</html>