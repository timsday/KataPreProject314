//JSON User service utilities
const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    findAllUsers: async () => await fetch('/api/users'),
    findUserById: async (id) => await fetch(`/api/users/${id}`),
    findCurrentUser: async () => await fetch(`/api/user`),
    saveUser: async (user) => await fetch('/api/users', {
        method: 'POST', headers: userFetchService.head, body: JSON.stringify(user)
    }),
    updateUser: async (user) => await fetch(`/api/users`, {
        method: 'PUT', headers: userFetchService.head, body: JSON.stringify(user)
    }),
    deleteUser: async (id) => await fetch(`/api/users/${id}`, {
        method: 'DELETE', headers: userFetchService.head})
}

//Tabs onload filling
tbodyAllUsers();
tbodyCurrentUser();

//Tabs content
async function tbodyAllUsers() {
    let tbody = $('#usersTable tbody');
    tbody.empty();
    userFetchService.findAllUsers().then(res => {
        res.json().then(users => {
            if (users.length > 0) {
                users.forEach(user => {
                    let userRow = `$(
                        <tr id="${user.id}" class="align-middle">
                        <th class="align-middle">${user.id}</th>
                        <td class="align-middle">${user.username}</td>
                        <td class="align-middle">${user.lastName}</td>
                        <td class="align-middle">${user.age}</td>
                        <td class="align-middle">${user.email}</td>
                        <td class="align-middle">${rolesNamesString(user.roles)}</td>
                        <td class="align-middle">
                            <button type="button"
                                    class="btn btn-info"
                                    data-toggle="modal"
                                    data-target="#modalEdit"
                                    onclick="editModalFilling(${user.id})"
                                    data-tip="edit">Edit
                            </button>
                        </td>
                        <td class="align-middle">
                            <button type="button"
                                    class="btn btn-danger"
                                    data-toggle="modal"
                                    data-target="#modalDelete"
                                    onclick="deleteModalFilling(${user.id})"
                                    data-tip="delete">Delete
                            </button>
                        </td>
                        </tr>)`;
                    tbody.append(userRow);
                })
            }
        })
    })
}

async function tbodyCurrentUser() {
    let tbody = $('#userTable tbody');
    tbody.empty();
    userFetchService.findCurrentUser().then(res => {
        res.json().then(user => {
            let userRow = `$(
                <tr id="${user.id}">
                <th class="align-middle">${user.id}</th>
                <td class="align-middle">${user.username}</td>
                <td class="align-middle">${user.lastName}</td>
                <td class="align-middle">${user.age}</td>
                <td class="align-middle">${user.email}</td>
                <td class="align-middle">${rolesNamesString(user.roles)}</td>
                </tr>)`;
            tbody.append(userRow);
        })
    })
}

//Roles column filling
function rolesNamesString(roles) {
    let rolesNamesString = '';
    roles.forEach(role => {
        rolesNamesString += role.roleName.replace('ROLE_', ' ');
    })
    return rolesNamesString.trim();
}

//Modals onload filling
function editModalFilling(id) {
    userFetchService.findUserById(id).then(res => {
        res.json().then(user => {
            // document.getElementById('editId').value = user.id
            // document.getElementById('editUsername').value = user.username
            // document.getElementById('editLastName').value = user.lastName
            // document.getElementById('editAge').value = user.age
            // document.getElementById('editEmail').value = user.email
            // document.getElementById('editPassword').value = user.password
            $("#editId").val(user.id);
            $("#editUsername").val(user.username);
            $("#editLastName").val(user.lastName);
            $("#editAge").val(user.age);
            $("#editEmail").val(user.email);
            $("#editPassword").val(user.password);
        });
    });
}

function deleteModalFilling(id) {
    userFetchService.findUserById(id).then(res => {
        res.json().then(user => {
            // document.getElementById('deleteId').value = user.id
            // document.getElementById('deleteUsername').value = user.username
            // document.getElementById('deleteLastName').value = user.lastName
            // document.getElementById('deleteAge').value = user.age
            // document.getElementById('deleteEmail').value = user.email
            // document.getElementById('deletePassword').value = user.password
            $("#deleteId").val(user.id);
            $("#deleteUsername").val(user.username);
            $("#deleteLastName").val(user.lastName);
            $("#deleteAge").val(user.age);
            $("#deleteEmail").val(user.email);
            $("#deletePassword").val(user.password);
        });
    });
}

//Admin-user shifting
document.querySelector('#currentUserLink')
    .addEventListener('click', async (e) => {
        $('#tabsPanel').hide();
        document.querySelector('#page-title').innerHTML = "User page";
        document.querySelector('#right-column-title').innerHTML = "User information-page";
    });

document.querySelector('#adminLink')
    .addEventListener('click', async (e) => {
        $('#tabsPanel').show();
        document.querySelector('#page-title').innerHTML = "Admin page";
        document.querySelector('#right-column-title').innerHTML = "Admin panel";
    });

//Edit event
document.querySelector('#editEventButton')
    .addEventListener('click', async (e) => {
        e.preventDefault()
        let editUser = {
            id: parseInt($('#editId').val()),
            username: $('#editUsername').val(),
            lastName: $('#editLastName').val(),
            age: parseInt($('#editAge').val()),
            email: $('#editEmail').val(),
            password: $('#editPassword').val(),
            stringRoles: $('#editRoles').val()
        }
        await userFetchService.updateUser(editUser, editUser.id);
        await userFetchService.findCurrentUser().then(res => {
            res.json().then(async user => {
                if (editUser.id === user.id) {
                    document.querySelector('#currentUserEmail')
                        .innerHTML = editUser.email;
                    document.querySelector('#currentUserRoles')
                        .innerHTML = editUser.stringRoles.join(' ').replaceAll('ROLE_', '');
                    await tbodyCurrentUser();
                }
            })
        })
        await tbodyAllUsers();
    });

//Delete event
document.querySelector('#deleteEventButton')
    .addEventListener('click', async (e) => {
        e.preventDefault()
        await userFetchService.deleteUser(parseInt($('#deleteId').val()));
        await tbodyAllUsers();
    });

//New user event
document.querySelector('#newUserSaveButton')
    .addEventListener('click', async (e) => {
        e.preventDefault()
        const newUser = {
            username: $('#newUsername').val(),
            lastName: $('#newLastName').val(),
            age: parseInt($('#newAge').val()),
            email: $('#newEmail').val(),
            password: $('#newPassword').val(),
            stringRoles: $('#newRoles').val()
        }
        await userFetchService.saveUser(newUser);
        await tbodyAllUsers();
        $('.nav-tabs a[href="#adminPanelTab"]').tab('show');
    });






