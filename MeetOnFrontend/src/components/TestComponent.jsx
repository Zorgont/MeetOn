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


export default class TemporaryDrawer extends React.Component{
  constructor(props) {
      super(props);
      this.state = {
        currentUser: AuthService.getCurrentUser(),
        classes: null,
        notifications: null,
        isNotificationListOpened: false
    }

    this.toggleDrawer.bind(this);
  }

  componentDidMount() {
    NotificationService.getNotificationsByUser(this.state.currentUser.id).then((res) => {
        this.setState({notifications: res.data});
    })
  }

  toggleDrawer = (open) => (event) => {
    if (event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }

    this.setState({isNotificationListOpened: open });
  };

  list = () => (
    <div
      role="presentation"
      onKeyDown={this.toggleDrawer(false)}>
        
      <List style={{width: "500px"}}> 
        {
            this.state.notifications?.map((notification, index) => (
            <div>
                <ListItem button key={index}>
                    <ListItemIcon>{index % 2 === 0 ? <InboxIcon /> : <MailIcon />}</ListItemIcon>
                    <ListItemText primary={notification.content} />
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
                    <Button onClick={this.toggleDrawer(true)}>Раскрыть</Button>
                    <Drawer anchor="right" open={this.state.isNotificationListOpened} onClose={this.toggleDrawer(false)}>
                        {this.list()}
                    </Drawer>
                </React.Fragment>
            }
            </div>
        );
    }
}