import React, { Component } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";
import GoogleLogin from "react-google-login";

import AuthService from "../services/AuthService";
import UserService from "../services/UserService";

const required = value => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                This field is required!
            </div>
        );
    }
};

export default class Login extends Component {
    constructor(props) {
        super(props);
        this.handleLogin = this.handleLogin.bind(this);
        this.onChangeUsername = this.onChangeUsername.bind(this);
        this.onChangePassword = this.onChangePassword.bind(this);

        this.state = {
            username: "",
            password: "",
            loading: false,
            message: ""
        };
    }

    onChangeUsername(e) {
        this.setState({
            username: e.target.value
        });
    }

    onChangePassword(e) {
        this.setState({
            password: e.target.value
        });
    }

    handleLogin(e) {
        e.preventDefault();

        this.setState({
            message: "",
            loading: true
        });

        this.form.validateAll();

        if (this.checkBtn.context._errors.length === 0) {
            AuthService.login(this.state.username, this.state.password).then(
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

    onSuccessfulGoogleAuthorization = (response) => {
        console.log(response);
        console.log("hell yeah");
        let user = {
            email: response.profileObj.email,
            firstName: response.profileObj.givenName,
            secondName: response.profileObj.familyName,
            accessToken: response.accessToken
        }
        console.log(`/usernameInput?email=${user.email}&firstName=${user.firstName}&secondName=${user.secondName}&accessToken=${user.accessToken}`)

        AuthService.existsUserByEmail(user.email).then(res => {
            console.log(res.data);
            if (res.data) {
                AuthService.loginViaGoogle(user).then(res => {
                    this.props.history.push("/profile")
                    window.location.reload();
                }, error => {
                    const resMessage =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                    this.setState({
                        loading: false,
                        message: resMessage
                    })
                });
            }
            else {
                this.props.history.push(`/usernameInput?email=${user.email}&firstName=${user.firstName}&secondName=${user.secondName}&accessToken=${user.accessToken}`);
                window.location.reload();
            }
        })
    }

    onFailureGoogleAuthorization = (response) => {
        console.log(response)
        console.log("damn it")
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

                    <GoogleLogin
                        clientId="9387373968-sqc6916e9o8h2usu5p981bdaj4sr4lu9.apps.googleusercontent.com"
                        buttonText="Join using Google"
                        onSuccess={this.onSuccessfulGoogleAuthorization}
                        onFailure={this.onFailureGoogleAuthorization}
                        cookiePolicy={'single_host_origin'}
                    />

                    <hr/>

                    <Form
                        onSubmit={this.handleLogin}
                        ref={c => {
                            this.form = c;
                        }}
                    >
                        <div className="form-group">
                            <label htmlFor="username">Username</label>
                            <Input
                                type="text"
                                className="form-control"
                                name="username"
                                value={this.state.username}
                                onChange={this.onChangeUsername}
                                validations={[required]}
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="password">Password</label>
                            <Input
                                type="password"
                                className="form-control"
                                name="password"
                                value={this.state.password}
                                onChange={this.onChangePassword}
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