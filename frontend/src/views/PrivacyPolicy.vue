<template>
    <div class="privacy-policy">
        <div class="container">
            <h1>Politique de Confidentialité</h1>
            <section>
                <h2>1. Collecte des Données</h2>
                <p>Nous collectons les informations suivantes :</p>
                <ul>
                    <li>Informations de navigation (cookies)</li>
                    <li>Données de connexion</li>
                    <li>Préférences utilisateur</li>
                </ul>
            </section>
            <section>
                <h2>2. Utilisation des Cookies</h2>
                <p>Les cookies sont utilisés pour :</p>
                <ul>
                    <li>Améliorer votre expérience de navigation</li>
                    <li>Mémoriser vos préférences</li>
                    <li>Analyser l'utilisation du site</li>
                </ul>
            </section>
            <section>
                <h2>3. Protection des Données</h2>
                <p>Nous nous engageons à :</p>
                <ul>
                    <li>Protéger vos données personnelles</li>
                    <li>Ne pas les partager avec des tiers non autorisés</li>
                    <li>Respecter le RGPD et les lois en vigueur</li>
                </ul>
            </section>
            <section>
                <h2>4. Vos Droits</h2>
                <p>Conformément au RGPD, vous disposez des droits suivants :</p>
                <ul>
                    <li>Droit d'accès à vos données</li>
                    <li>Droit de rectification</li>
                    <li>Droit à l'effacement</li>
                    <li>Droit à la portabilité</li>
                </ul>
            </section>
            <section>
                <h2>5. Contact</h2>
                <p>Pour toute question concernant vos données :</p>
                <ul>
                    <li>Email : privacy@example.com</li>
                    <li>Téléphone : +33 1 23 45 67 89</li>
                </ul>
            </section>
            <div class="actions"> <button @click="goBack" class="btn-back">Retour</button> </div>
        </div>
    </div>
</template>
<script>export default { name: 'PrivacyPolicy', methods: { goBack() { this.$router.go(-1) } } }</script>
<style
    scoped>
    .privacy-policy {
        padding: 2rem 0;
        background-color: #f9fafb;
        min-height: 100vh;
    }

    .container {
        max-width: 800px;
        margin: 0 auto;
        padding: 0 1rem;
    }

    h1 {
        color: #1f2937;
        font-size: 2.5rem;
        margin-bottom: 2rem;
        text-align: center;
    }

    section {
        background-color: white;
        padding: 2rem;
        margin-bottom: 2rem;
        border-radius: 8px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }

    h2 {
        color: #4f46e5;
        font-size: 1.5rem;
        margin-bottom: 1rem;
    }

    p {
        color: #4b5563;
        line-height: 1.6;
        margin-bottom: 1rem;
    }

    ul {
        list-style-type: disc;
        margin-left: 1.5rem;
        color: #4b5563;
    }

    li {
        margin-bottom: 0.5rem;
        line-height: 1.6;
    }

    .actions {
        text-align: center;
        margin-top: 2rem;
    }

    .btn-back {
        background-color: #4f46e5;
        color: white;
        padding: 0.75rem 1.5rem;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-weight: 500;
        transition: all 0.3s ease;
    }

    .btn-back:hover {
        background-color: #4338ca;
    }

    @media (max-width: 768px) {
        .privacy-policy {
            padding: 1rem 0;
        }

        h1 {
            font-size: 2rem;
        }

        section {
            padding: 1.5rem;
        }
    }
</style>
