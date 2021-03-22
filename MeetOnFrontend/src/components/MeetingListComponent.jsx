import React, { Component } from "react";
import { Link } from 'react-router-dom';
import MeetingService from '../services/MeetingService';

class MeetingList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            meetings: []
        }
    }
    
    componentDidMount() {
        MeetingService.getMeetings().then((res) => {
            this.setState({meetings: res.data});
            console.log(this.state.meetings);
        });
    }

    render() {
        return (
            <div>
                <h2 className="text-center">Meetings list</h2>
                <div className="row">
                    <table className="table table-striped table-bordered">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>About</th>
                                <th>Author name</th>
                                <th>Tags</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                this.state.meetings.map(
                                    meeting => 
                                    <tr key={meeting.id}>
                                        <td><Link to={`/meetings/${meeting.meetingId}`}>{meeting.name}</Link></td>
                                        <td> {meeting.about} </td>
                                        <td> <a href={`/users/${meeting.managerId}`}>{meeting.managerUsername} </a></td>
                                        <td>
                                        {
                                            meeting.tags.map(
                                                tag => 
                                                    <div style={{marginRight: "10px", background: "#ddd", padding: 3, borderRadius: "10px"}}> {tag}</div>
                                            )
                                        }
                                        </td>
                                    </tr>
                                )
                            }
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}

export default MeetingList;