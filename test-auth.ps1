# Test d'authentification
$baseUrl = "http://localhost:8080/api"

Write-Host "=== Test d'authentification ===" -ForegroundColor Green

# 1. Test de validation sans token (doit retourner 401)
Write-Host "1. Test validation sans token..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$baseUrl/auth/validate" -Method GET
    Write-Host "ERREUR: Devrait retourner 401" -ForegroundColor Red
} catch {
    Write-Host "SUCCÈS: 401 retourné comme attendu" -ForegroundColor Green
    Write-Host "Message: $($_.Exception.Message)" -ForegroundColor Gray
}

# 2. Test d'enregistrement
Write-Host "`n2. Test d'enregistrement..." -ForegroundColor Yellow
$registerData = @{
    email = "test@test.com"
    password = "Test123!"
    nom = "Test"
    prenom = "User"
    adresse = "123 Test St"
    codePostal = "12345"
    ville = "Test City"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/auth/register" -Method POST -Body $registerData -ContentType "application/json"
    Write-Host "SUCCÈS: Utilisateur enregistré" -ForegroundColor Green
    $accessToken = $response.accessToken
    Write-Host "Token reçu: $($accessToken.Substring(0, 20))..." -ForegroundColor Gray
} catch {
    Write-Host "ERREUR lors de l'enregistrement: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 3. Test de validation avec token valide
Write-Host "`n3. Test validation avec token valide..." -ForegroundColor Yellow
try {
    $headers = @{
        "Authorization" = "Bearer $accessToken"
        "Content-Type" = "application/json"
    }
    $response = Invoke-RestMethod -Uri "$baseUrl/auth/validate" -Method GET -Headers $headers
    Write-Host "SUCCÈS: Token validé" -ForegroundColor Green
    Write-Host "Username: $($response.username)" -ForegroundColor Gray
} catch {
    Write-Host "ERREUR lors de la validation: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== Test terminé ===" -ForegroundColor Green 