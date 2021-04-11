import React from 'react';
import Drawer from '@material-ui/core/Drawer';
import Button from '@material-ui/core/Button';
import List from '@material-ui/core/List';
import Divider from '@material-ui/core/Divider';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import InboxIcon from '@material-ui/icons/MoveToInbox';
import MailIcon from '@material-ui/icons/Mail';
import AuthService from "../services/AuthService";
import NotificationService from "../services/NotificationService";
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import NotificationsIcon from '@material-ui/icons/Notifications';
export default class TemporaryDrawer extends React.Component{
  constructor(props) {
      super(props);
      this.state = {
        currentUser: AuthService.getCurrentUser(),
        classes: null,
        notifications: null,
        isNotificationListOpened: false,
        unviewedAmount: 0
    }
    
    this.toggleDrawer.bind(this);
  }

  componentDidMount() {
    NotificationService.getNotificationsByUser(this.state.currentUser.id).then((res) => {
        this.setState({notifications: res.data});
        // eslint-disable-next-line array-callback-return
        this.setState({
            unviewedAmount: this.state.notifications.filter(notification => {return notification.status==="UNVIEWED"}).length})
    })

  }

  toggleDrawer = (open) => (event) => {
    if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }

    this.setState({isNotificationListOpened: open });
  };

  checkViewed=(status) => {
     return status==="UNVIEWED"?{backgroundColor:"#dadada"}:{backgroundColor: "white"};
  }
  setViewed=(index) => {
      if(this.state.notifications[index].status==="UNVIEWED"){
          NotificationService.setNotificationStatusViewed(this.state.notifications[index].id).then((res) => {
              this.state.notifications[index]=res.data;
              this.setState({
                  notifications: this.state.notifications,
                  unviewedAmount: this.state.unviewedAmount-1
              });
          })
      }
  }

  list = () => (
    <div
      role="presentation"
      onKeyDown={this.toggleDrawer(false)} style={{top: "55px"}}>
        
      <List style={{width: "500px"}}> 
      {
        this.state.notifications?.map((notification, index) => (

        <div>
            {/* <ListItem button key={index} style={this.checkViewed(notification.status)}>
                <ListItemIcon>{index % 2 === 0 ? <InboxIcon /> : <MailIcon />}</ListItemIcon>
                <ListItemText primary={notification.content} />
            </ListItem> */}
            <ListItem button key={index} style={this.checkViewed(notification.status)} onClick={this.setViewed.bind(this,index)}>
                <ListItemText>
                  <p>{notification.content}</p>
                  <p style={{color: "#aaa", fontSize: "12px", margin: "10px 0 0 0"}}>{notification.date}</p>
                </ListItemText>
            </ListItem>
            <Divider />
        </div>
        ))
      }
      </List>
    </div>
  );

  render() {
      return (
          <div>
          {
            <React.Fragment>
                <IconButton aria-label="show 4 new mails" color="inherit" onClick={this.toggleDrawer(true)}>
                <Badge badgeContent={this.state.unviewedAmount} color="secondary">
                    <NotificationsIcon/>
                </Badge>
            </IconButton>
                <Drawer anchor="right" style={{flexShrink: 0}}
                open={this.state.isNotificationListOpened}
                  onClose={this.toggleDrawer(false)}>
                    {this.list()}
                </Drawer>
            </React.Fragment>
          }
          </div>
      );
  }
}
