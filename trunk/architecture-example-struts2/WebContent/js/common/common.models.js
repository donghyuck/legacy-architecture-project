var _TWITTER_FEED_URL = "/community/get-twitter-hometimeline.do?output=json",
	_FACEBOOK_FEED_URL    = "/community/get-facebook-homefeed.do?output=json",
	_TWITTER_FEED_DATA  = "homeTimeline",
	_FACEBOOK_FEED_DATA = "homeFeed",	
	MediaStreams = kendo.Class.extend({
		mediaId : 0,
		name : null,
		data : null,
		url : null,
		dataSource : null,		
		template: null,
		init: function(mediaId, name, url, data) {
			if (name) this.name = name;				
			if( name == 'twitter' )
			{
				this.url = _TWITTER_FEED_URL;
				this.data = _TWITTER_FEED_DATA;
			}
			else if ( name == 'facebook' ){
				this.url = _FACEBOOK_FEED_URL;
				this.data = _FACEBOOK_FEED_DATA;								
			}			
			if (url) this.url = url;
			if (data) this.data = data;
		},
		setDataSource: function ( options ){
			this.dataSource = new kendo.data.DataSource({
				transport: {
					read: {
						type : 'POST',
						type: "json",
						url : this.url
					} 
				},
				error:handleKendoAjaxError,
				schema: {
					data : this.data
				}
			});		
/**
			if( options.requestStart ){				
				this.dataSource.bind( 'requestStart' , options.requestStart );
			}else{
				this.dataSource.bind( 'requestStart' , kendo.ui.progress( this.elementToRender(), true) );
			}	
			
			if( options.requestEnd ){
				this.dataSource.bind( 'requestEnd' , options.requestEnd );
			}else{
				this.dataSource.bind( 'requestEnd' , kendo.ui.progress( this.elementToRender(), false) );
			}			
			if( options.change ){
				this.dataSource.bind( 'change' , options.change );
			}else{
				this.dataSource.bind( 'change' , function (){ 
					alert("fdasf");
					this.elementToRender().html(kendo.render( this.template, this.dataSource.view()));
					}
				);
			}		
			*/	
		},
		setTemplate: function ( template ){
			if (template) this.template = template;	
		},
		renderToString : function () {
			return  "#"+ this.name + "-streams-" + this.mediaId ;
		},
		elementToRender : function (){
			return $(this.renderToString);
		}
	});	
	

var Announce = kendo.data.Model.define( {
    id: "announceId", // the identifier of the model
    fields: {
    	announceId: { type: "number", editable: false, defaultValue: -1  },
    	subject: { type: "string", editable: true },
    	body: { type: "string", editable: true },
    	startDate: { type: "date",  editable: true },
    	endDate: { type: "date" ,  editable: true},
        modifiedDate: { type: "date"},
        creationDate: { type: "date" }
    }
});

var OAuthToken = kendo.data.Model.define( {
    id: "socialAccountId", // the identifier of the model
    fields: {
    	socialAccountId: { type: "number", editable: false, defaultValue: -1  },    	
    	objectType: { type: "number", editable: false, defaultValue: -1  },    	
    	objectId: { type: "number", editable: false, defaultValue: -1  },    	
    	serviceProviderName: { type: "string", editable: true , validation: { required: true }},
    	authorizationUrl : { type: "string", editable: false  },    	
    	accessSecret: { type: "string", editable: true },
    	accessToken : { type: "string", editable: true },
    	signedIn: { type:"boolean", defaultVlaue: false },
        modifiedDate: { type: "date"},
        creationDate: { type: "date" } 
    }
});

var SocialNetwork = kendo.data.Model.define( {
    id: "socialAccountId", // the identifier of the model
    fields: {
    	socialAccountId: { type: "number", editable: false, defaultValue: -1  },    	
    	objectType: { type: "number", editable: false, defaultValue: -1  },    	
    	objectId: { type: "number", editable: false, defaultValue: -1  },    	
    	serviceProviderName: { type: "string", editable: true , validation: { required: true }},
    	authorizationUrl : { type: "string", editable: false  },    	
    	accessSecret: { type: "string", editable: true },
    	accessToken : { type: "string", editable: true },
    	signedIn: { type:"boolean", defaultVlaue: false },
    	connected: { type:"boolean", defaultVlaue: false },
        modifiedDate: { type: "date"},
        creationDate: { type: "date" } 
    }
});

var SocialAccount = kendo.data.Model.define( {
    id: "socialAccountId", // the identifier of the model
    fields: {
    	socialAccountId: { type: "number", editable: false, defaultValue: -1  },    	
    	objectType: { type: "number", editable: false, defaultValue: -1  },    	
    	objectId: { type: "number", editable: false, defaultValue: -1  },    	
    	serviceProviderName: { type: "string", editable: true , validation: { required: true }},
    	authorizationUrl : { type: "string", editable: false  },    	
    	accessSecret: { type: "string", editable: true },
    	accessToken : { type: "string", editable: true },
    	signedIn: { type:"boolean", defaultVlaue: false },
        modifiedDate: { type: "date"},
        creationDate: { type: "date" } 
    }
});

var Template = kendo.data.Model.define( {
    id: "templateId", // the identifier of the model
    fields: {
    	templateId: { type: "number", editable: false, defaultValue: -1  },    	
    	objectType: { type: "number", editable: false, defaultValue: -1  },    	
    	objectId: { type: "number", editable: false, defaultValue: -1  },    	
        title: { type: "string", editable: true , validation: { required: true }},
        templateType: { type: "string", editable: true },
        location : { type: "string", editable: true },
        body: { type: "string", editable: true },
        modifiedDate: { type: "date"},
        creationDate: { type: "date" } 
    }
});

var Image = kendo.data.Model.define( {
    id: "imageId", // the identifier of the model
    fields: {
    	imageId: { type: "number", editable: false, defaultValue: -1  },   
    	objectType: { type: "number", editable: false, defaultValue: -1  },    	
    	objectId: { type: "number", editable: false, defaultValue: -1  },    	
    	name: { type: "string", editable: true , validation: { required: true }},
        contentType: { type: "string", editable: false },
        imageId: { type: "number", editable: false },
        modifiedDate: { type: "date"},
        creationDate: { type: "date" },
        index : {type: "number", defaultValue : 0 }
    }
});

var Attachment = kendo.data.Model.define( {
    id: "attachmentId", // the identifier of the model
    fields: {
    	attachmentId: { type: "number", editable: false, defaultValue: -1  },   
    	objectType: { type: "number", editable: false, defaultValue: -1  },    	
    	objectId: { type: "number", editable: false, defaultValue: -1  },    	
    	name: { type: "string", editable: true , validation: { required: true }},
        contentType: { type: "string", editable: false },
        downloadCount: { type: "number", editable: false },
        modifiedDate: { type: "date"},
        creationDate: { type: "date" }        
    }
});

var Company = kendo.data.Model.define( {
    id: "companyId", // the identifier of the model
    fields: {
    	companyId: { type: "number", editable: false, defaultValue: -1  },    	
        name: { type: "string", editable: true , validation: { required: true }},
        displayName: { type: "string", editable: true },
        domainName: { type: "string", editable: true },
        description: { type: "string", editable: true },
        modifiedDate: { type: "date"},
        creationDate: { type: "date" },
        memberCount: { type: "number", editable: true, defaultValue: 0  },
        adminCount: { type: "number", editable: true, defaultValue: 0  }
    }
});


var User = kendo.data.Model.define( {
    id: "userId", // the identifier of the model
    fields: {
    	companyId: {  type: "number", defaultValue: 1 },
    	company: Company,
    	userId: { type: "number", editable: true, defaultValue: -1  },
        username: { type: "string", editable: true },
        name: { type: "string", editable: true },
        email: { type: "string" , editable: true },
        password: { type: "string" , editable: true },
        creationDate: { type: "date" },
        modifiedDate: { type: "date" },
        lastLoggedIn: { type: "date" },
        lastProfileUpdate : { type: "date" },                                    
        enabled : {type: "boolean" },
        nameVisible : {type: "boolean" },	        
        emailVisible: {type: "boolean" },
        hasProfileImage:{type: "boolean", defaultValue: false},
        formattedLastLoggedIn : { type: "string" },
        formattedLastProfileUpdate : { type: "string" },
        isSystem: { type:"boolean", defaultVlaue: false },
        anonymous : { type:"boolean", defaultVlaue: true },
        photoUrl : {type: "string", editable: true, defaultVlaue: null },
        roles: {}
    },
    hasRole : function ( role ) {
    	if( typeof( user.roles ) != "undefined" && $.inArray( role, user.roles ) >= 0 )
			return true
		else 
			return false;    	
    }
});

var Group = kendo.data.Model.define( {
    id: "groupId", // the identifier of the model
    fields: {
    	companyId: { type: "number", defaultValue: 1 },
    	company: Company,
        groupId: { type: "number", editable: false, defaultValue: -1  },
        name: { type: "string", editable: true, validation: { required: true }},
        description: { type: "string", editable: true },
        modifiedDate: { type: "date"},
        creationDate: { type: "date" },
        memberCount: { type: "number", editable: true, defaultValue: 0  },
        adminCount: { type: "number", editable: true, defaultValue: 0  },
        clear: function() {
            this.set("groupId", 0 );
            this.set("name", "");
            this.set("description", "");
            this.set("modifiedDate", null ); 
            this.set("creationDate", null );
        }        
    }
});

var Property = kendo.data.Model.define( {
    id: "name", // the identifier of the model
    fields: {
    	name: { type: "string",  editable: true },
    	value:  { type: "string", editable: true }     
    }
});

var Role = kendo.data.Model.define( {
    id: "roleId", // the identifier of the model
    fields: {
    	roleId: { type: "number", editable: false, defaultValue: -1  },
        name: { type: "string", editable: true, validation: { required: true }},
        description: { type: "string", editable: true },
        modifiedDate: { type: "date"},
        creationDate: { type: "date" }
    }
});

var DatabaseInfo = kendo.data.Model.define( {
    fields: {
    	databaseName: { type: "string",  editable: false },
    	databaseVersion:  { type: "string", editable: false },
    	driverName : { type: "string", editable: false},
    	driverVersion: {type: "string", editable: false},
    	isolationLevel: {type: "string", editable: false}
    }
});

var Menu = kendo.data.Model.define( {
	  	id: "menuId", // the identifier of the model
	    fields: {
	    	menuId: { type: "number", editable: false, defaultValue: -1  }, 
	    	name: { type: "string", editable: true },
	        title: { type: "string", editable: true },
	        enabled : {type: "boolean" },
	        description: { type: "string", editable: true },
	        properties : {},
	        menuData : { type: "string", editable: true, defaultValue : "" },
	        modifiedDate: { type: "date"},
	        creationDate: { type: "date" }	        
	    }	
});

