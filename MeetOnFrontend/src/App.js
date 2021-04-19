import React, { Component } from "react";
import { Switch, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import AuthService from "./services/AuthService";

import Login from "./components/LoginComponent";
import Register from "./components/RegisterComponent";
import Profile from "./components/ProfileComponent";
import BoardUser from "./components/BoardUserComponent";
import BoardModerator from "./components/BoardUserComponent";
import BoardAdmin from "./components/BoardUserComponent";
import CreateMeeting from "./components/CreateMeetingComponent";
import MeetingList from "./components/MeetingListComponent";
import MeetingPage from "./components/MeetingPageComponent";
import UpdateMeeting from "./components/ChangeMeetingComponent";
import CreateRequest from "./components/CreateRequestComponent";
import MeetingRequest from "./components/MeetingRequestComponent";
import NotificationBar from "./components/NotificationBarComponent";
import ProfileSettings from "./components/ProfileSettingsComponent";

class App extends Component {
    constructor(props) {
        super(props);
        this.logOut = this.logOut.bind(this);

        this.state = {
            showModeratorBoard: false,
            showAdminBoard: false,
            currentUser: undefined,
        };
    }

    componentDidMount() {
        const user = AuthService.getCurrentUser();

        if (user) {
            this.setState({
                currentUser: user,
                showModeratorBoard: user.roles.includes("ROLE_MODERATOR"),
                showAdminBoard: user.roles.includes("ROLE_ADMIN"),
            });
        }
    }

    logOut() {
        AuthService.logout();
    }

    render() {
        const { currentUser, showModeratorBoard, showAdminBoard } = this.state;

        return (
            <div>
                <nav className="navbar navbar-expand navbar-dark bg-dark">
                    <Link to={"/"} className="navbar-brand">
                        MeetOn
                    </Link>
                    <div className="navbar-nav mr-auto">
                        <li className="nav-item">
                            <Link to={"/home"} className="nav-link">
                                Home
                            </Link>
                        </li>
                        <li className="nav-item">
                            <Link to={"/meetings"} className="nav-link">
                                Meetings
                            </Link>
                        </li>

                        {showModeratorBoard && (
                            <li className="nav-item">
                                <Link to={"/mod"} className="nav-link">
                                    Moderator Board
                                </Link>
                            </li>
                        )}

                        {showAdminBoard && (
                            <li className="nav-item">
                                <Link to={"/admin"} className="nav-link">
                                    Admin Board
                                </Link>
                            </li>
                        )}

                        {currentUser && (
                            <li className="nav-item">
                                <Link to={"/user"} className="nav-link">
                                    User
                                </Link>
                            </li>
                        )}
                    </div>

                    {currentUser ? (
                        <div className="navbar-nav ml-auto">
                            <li className="nav-item">
                                <Link to={"/profile"} className="nav-link">
                                    {currentUser.username}
                                </Link>
                            </li>
                            <li className="nav-item">
                                <NotificationBar></NotificationBar>
                            {/*Чтобы сделать уведомления под шапкой,необходимо заменить navbar из bootstrap нв AppBar из Material,тогда это решается так:
                                https://stackoverflow.com/questions/49051975/material-ui-drawer-wont-move-under-appbar =
                                Также в Bootstrap,оказывается,есть свой drawer(offcanvas),можно заменить drawer на него.*/}
                            </li>
                            <li className="nav-item">
                                <a href="/login" className="nav-link" onClick={this.logOut}>
                                    LogOut
                                </a>
                            </li>

                        </div>
                    ) : (
                        <div className="navbar-nav ml-auto">
                            <li className="nav-item">
                                <Link to={"/login"} className="nav-link">
                                    Login
                                </Link>
                            </li>

                            <li className="nav-item">
                                <Link to={"/register"} className="nav-link">
                                    Sign Up
                                </Link>
                            </li>
                        </div>
                    )}
                </nav>

                <div className="container mt-3">
                    <Switch>
                        <Route exact path={["/", "/home"]} component={MeetingList} />
                        <Route exact path="/login" component={Login} />
                        <Route exact path="/register" component={Register} />
                        <Route exact path="/profile" component={Profile} />
                        <Route exact path="/profile/update" component={ProfileSettings} />
                        <Route exact path="/user" component={BoardUser} />
                        <Route exact path="/mod" component={BoardModerator} />
                        <Route exact path="/admin" component={BoardAdmin} />
                        <Route exact path="/add_meeting" component={CreateMeeting} />
                        <Route exact path="/meetings" component={MeetingList} />
                        <Route path="/meetings/:id" component={MeetingPage} />
                        <Route path="/meetings/:id/requests" component={MeetingRequest} />
                        <Route path="/update/:id" component={UpdateMeeting} />
                        <Route path="/enroll/:id" component={CreateRequest} />
                    </Switch>
                </div>
            </div>
        );
    }
}

export default App;