import React from 'react';
import Button from '@material-ui/core/Button';
import Snackbar from '@material-ui/core/Snackbar';
import MuiAlert from '@material-ui/lab/Alert';

export default function NotificationsAlert() {
    const [open, setOpen] = React.useState(false);

    const handleClick = () => {
        setOpen(true);
    };

    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }

        setOpen(false);
    };

    return (
        <div>
            <Button onClick={handleClick}>Open simple snackbar</Button>
            <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}  anchorOrigin={{vertical: 'bottom',horizontal: 'left',}} >
                    <MuiAlert elevation={6} variant="filled" onClose={handleClose} severity="info">
                        New notifications!
                    </MuiAlert>
            </Snackbar>
        </div>
    );
}