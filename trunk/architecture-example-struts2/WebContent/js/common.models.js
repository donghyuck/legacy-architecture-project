var User = kendo.data.Model.define( {
    id: "userId", // the identifier of the model
    fields: {
    	userId: { type: "number", editable: true, defaultValue: -1  },
        username: { type: "string", editable: true },
        name: { type: "string", editable: true },
        email: { type: "string" , editable: true },
        creationDate: { type: "date" },
        lastLoggedIn: { type: "date" },
        lastProfileUpdate : { type: "date" },                                    
        enabled : {type: "boolean" },
        nameVisible : {type: "boolean" },	
        emailVisible: {type: "boolean" },
        formattedLastLoggedIn : { type: "string" },
        formattedLastProfileUpdate : { type: "string" },
        clear: function() {
            this.set("userId", 0 );
            this.set("username", "");
            this.set("name", "");
            this.set("email", false);
            this.set("creationDate", null ); 
            this.set("lastLoggedIn", null ); 
            this.set("lastProfileUpdate", null ); 
            this.set("formattedLastLoggedIn", "" ); 
            this.set("formattedLastProfileUpdate", "" );   
            this.set("enabled", false ); 
            this.set("nameVisible", false ); 
            this.set("emailVisible", false );
        }        
    }
});

var Group = kendo.data.Model.define( {
    id: "groupId", // the identifier of the model
    fields: {
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
    	name: { type: "string" },
    	value: { type: "string" }     
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
