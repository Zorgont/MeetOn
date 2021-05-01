import React, { Component } from 'react';
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import UserService from '../services/UserService';
import AuthService from "../services/AuthService";

const required = value => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                This field is required!
            </div>
        );
    }
};

class UsernameInputComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            email: '',
            username:'',
            firstName: '',
            secondName: '',
            accessToken: ''
        }
        this.handleLogin = this.handleLogin.bind(this);
        this.onChangeUsername = this.onChangeUsername.bind(this);
    }
    
    componentDidMount() {
        const params = new URLSearchParams (this.props.location.search);

        this.state.email = params.get('email')
        this.state.firstName = params.get('firstName')
        this.state.secondName = params.get('secondName')
        this.state.accessToken = params.get('accessToken')
        this.setState({
            email: this.state.email,
            firstName: this.state.firstName,
            secondName: this.state.secondName,
            accessToken: this.state.accessToken
        })
    }

    onChangeUsername(e) {
        this.setState({
            username: e.target.value
        });
    }

    handleLogin(e) {
        e.preventDefault();

        this.setState({
            message: "",
            loading: true
        });

        this.form.validateAll();

        let user = {
            email: this.state.email,
            username: this.state.username,
            firstName: this.state.firstName,
            secondName: this.state.secondName,
            accessToken: this.state.accessToken
        }

        if (this.checkBtn.context._errors.length === 0) {
            AuthService.loginViaGoogle(user).then(
                () => {
                    this.props.history.push("/profile");
                    window.location.reload();
                },
                error => {
                    const resMessage =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();
    
                    this.setState({
                        loading: false,
                        message: resMessage
                    });
                }
            );
        } else {
            this.setState({
                loading: false
            });
        }
    }

    render() {
        return (
            <div className="col-md-12">
                <div className="card card-container">
                    <img
                        src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
                        alt="profile-img"
                        className="profile-img-card"
                    />

                    <Form onSubmit={this.handleLogin} ref={c => { this.form = c; }}>
                        <div className="form-group">
                            <label htmlFor="username">Username</label>
                            <Input type="text" className="form-control" name="username"
                                value={this.state.username} onChange={this.onChangeUsername}
                                validations={[required]}
                            />
                        </div>

                        <div className="form-group">
                            <button
                                className="btn btn-primary btn-block"
                                disabled={this.state.loading}
                            >
                                {this.state.loading && (
                                    <span className="spinner-border spinner-border-sm"></span>
                                )}
                                <span>Login</span>
                            </button>
                        </div>

                        {this.state.message && (
                            <div className="form-group">
                                <div className="alert alert-danger" role="alert">
                                    {this.state.message}
                                </div>
                            </div>
                        )}
                        <CheckButton
                            style={{ display: "none" }}
                            ref={c => {
                                this.checkBtn = c;
                            }}
                        />
                    </Form>
                </div>
            </div>
        );
    }
}

export default UsernameInputComponent;