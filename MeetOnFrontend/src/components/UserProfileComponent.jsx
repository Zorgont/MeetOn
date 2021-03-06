import React, { Component } from "react";
import Chip from '@material-ui/core/Chip';
import MailOutlineIcon from '@material-ui/icons/MailOutline';
import EditOutlinedIcon from '@material-ui/icons/EditOutlined';
import AuthService from "../services/AuthService";
import MeetingService from "../services/MeetingService";
import RequestService from "../services/RequestService";
import Avatar from '@material-ui/core/Avatar';
import UserService from "../services/UserService";
import TagGroupService from "../services/TagGroupService";
import MeetingCardComponent from "./MeetingCardComponent";

export default class UserProfileComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: AuthService.getCurrentUser(),
            meetings: [],
            requests: null,
            notifications: [],
            user: null,
            tagGroups: null
        };
    }

    componentDidMount() {
        let id = Number(this.props.match.params.userId ? this.props.match.params.userId : this.state.currentUser.id);
        console.log("dasdasdasdd")
        console.log(id)
        UserService.getUserById(id).then(res => {
            this.setState({user: res.data})
        });

        MeetingService.getMeetingsByManager(id).then((res) => {
            this.setState({ meetings: res.data })
            console.log(res.data)
        });

        TagGroupService.getTagGroups(id).then(res => {
            this.setState({tagGroups: res.data})
        });

        if (this.state.currentUser !== null && id === this.state.currentUser.id)
            RequestService.getRequestsByUserId(id).then((res) => {
                let requests = []
                for (let index in res.data) {
                    let request = res.data[index];
                    if (request.role === "MANAGER")
                        continue;
                    MeetingService.getMeetingById(request.meeting_id).then(res => {
                        let meeting = res.data
                        console.log(meeting)
                        requests.push({
                            request: request,
                            meeting: meeting
                        })
                        this.setState({requests: requests})
                    })
                }
            })
    }

    componentDidUpdate(prevProps) {
        if (this.props.match.params.userId !== prevProps.match.params.userId)
            window.location.reload();
    }

    editProfileClicked(event) {
        this.props.history.push('/profile/update');
    }

    render() {
        const { classes } = this.props;
        const { user, tagGroups, meetings, requests } = this.state;
        console.log(requests)
        console.log(user?.id)
        return (
            <div className="container">
                <div className="row mt-5">
                    <div className="col-3">
                        <div className="row shadow-container">
                            <div className="col mt-4 mb-2">
                                <div className="row">
                                    <div className="col d-flex justify-content-center">
                                        <div style={{position: "relative"}}>
                                            <Avatar style={{width: "130px", height: "130px"}} src={this.state.user !== null ? `https://meetonapi.herokuapp.com/api/v1/users/${this.state.user?.id}/avatar` : ""}/>
                                            {user?.id === this.state.currentUser?.id && <div onClick={this.editProfileClicked.bind(this)} style={{position: "absolute", right: "12px", width: "30px", height: "30px", bottom: "-5px", backgroundColor: "white", borderRadius: "50px", cursor: "pointer"}} className="gradient-gray-border">
                                                <EditOutlinedIcon style={{margin: "0 0 0 3px"}} />
                                            </div>}
                                        </div>
                                    </div>
                                </div>
                                <div className="row mt-2">
                                    <div className="col text-center">
                                        {user?.firstName || user?.secondName ?
                                        <h5>{user?.firstName} {user?.secondName}</h5> : 
                                        <h5>Anonimus</h5>
                                        }
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col d-flex justify-content-center">
                                        <Chip variant="outlined" icon={<MailOutlineIcon/>} label={user?.email}></Chip>
                                    </div>
                                </div>
                                <div className="row mt-4">
                                    <div className="col">
                                        <p>User name</p>
                                        <p>Karma</p>
                                    </div>
                                    <div className="col text-right">
                                        <p>{user?.username}</p>
                                        <p>{Math.round(parseFloat(user?.karma))}</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row shadow-container mt-3">
                            <div className="col mt-3 mb-3">
                                <div className="row">
                                    <div className="col text-center">
                                        <h5>About</h5>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="col">
                                        <p>{user?.about ? user?.about : "No information"}</p>
                                    </div>
                                </div>
                                <div className="row mt-2">
                                    <div className="col text-center">
                                        <h5>Interests</h5>
                                    </div>
                                </div>
                                {tagGroups !== null && tagGroups.length > 0 ? tagGroups.map((tagGroup, index) =>
                                    <div className="row">
                                        <div className="col">
                                            <span>{index + 1}</span>
                                            
                                            {tagGroup.tags.map(tag =>
                                                <Chip style={{backgroundColor: "#636363", color: "#fff", fontSize: "11px"}} className="mt-1 ml-1 mr-1" label={tag}/>
                                            )}
                                        </div>
                                    </div>
                                ) :
                                <p>No interests provided</p>
                                }
                            </div>
                        </div>
                    </div>
                    <div className="col"/>
                    <div className="col-8">
                        <div className="row shadow-container">
                            <div className="col mt-3 mb-3">
                                <div className="row">
                                    <div className="col">
                                        <h3>{requests ? "My meetings" : "Meetings"}</h3>
                                    </div>
                                </div> 
                                <div className="row">
                                    {meetings?.sort((a,b) => new Date(a.date) - new Date(b.date)).map(meeting =>
                                        <div className="col-6 mt-3">
                                            <MeetingCardComponent meeting={meeting} height="250px"/>
                                        </div>
                                    )}
                                </div>
                                {requests &&
                                    <div className="row">
                                        <div className="col mt-3">
                                            <hr/>
                                            <h3>My requests</h3>
                                        </div>
                                    </div> 
                                }
                                {requests &&
                                    <div className="row">{console.log(requests)}
                                        {requests?.filter(request => request.request.user_id !== request.meeting.managerId).sort((a,b) => new Date(a.meeting.date) - new Date(b.meeting.date)).map(request =>
                                            <div className="col-6 mt-3">
                                                <MeetingCardComponent request={request.request} meeting={request.meeting} height="285px"/>
                                            </div>
                                        )}
                                    </div>
                                }
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}