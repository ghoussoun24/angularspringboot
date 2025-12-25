describe('User Registration and Login E2E Test', () => {
  const baseUrl = 'http://localhost:8081'
  let userEmail = `testuser${Date.now()}@example.com`
  let userPassword = 'password123'

  it('should register a new user and then login', () => {
    // Step 1: Register a new user
    cy.request({
      method: 'POST',
      url: `${baseUrl}/api/v1/auth/register`,
      body: {
        firstname: 'Test',
        lastname: 'User',
        email: userEmail,
        password: userPassword,
        role: 'USER'
      },
      failOnStatusCode: false
    }).then((response) => {
      expect(response.status).to.eq(200)
      expect(response.body).to.have.property('token')
      expect(response.body).to.have.property('user')
    })

    // Step 2: Authenticate (login) with the registered user
    cy.request({
      method: 'POST',
      url: `${baseUrl}/api/v1/auth/authenticate`,
      body: {
        email: userEmail,
        password: userPassword
      }
    }).then((response) => {
      expect(response.status).to.eq(200)
      expect(response.body).to.have.property('token')
      expect(response.body).to.have.property('user')
      expect(response.body.user.email).to.eq(userEmail)
    })
  })

  it('should fail to register with existing email', () => {
    // Try to register again with the same email
    cy.request({
      method: 'POST',
      url: `${baseUrl}/api/v1/auth/register`,
      body: {
        firstname: 'Test2',
        lastname: 'User2',
        email: userEmail,
        password: 'password456',
        role: 'USER'
      },
      failOnStatusCode: false
    }).then((response) => {
      expect(response.status).to.eq(400) // Assuming it returns 400 for duplicate email
    })
  })
})
