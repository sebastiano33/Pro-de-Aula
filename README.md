# =========================
# CONFIGURACIÓN INICIAL
# =========================
git config --global user.name "Tu nombre"
git config --global user.email "tuemail@example.com"
git config --list

# =========================
# REPOSITORIOS
# =========================
git init
git clone https://github.com/usuario/repositorio.git

# =========================
# ESTADO Y CAMBIOS
# =========================
git status
git add archivo.txt
git add .
git commit -m "Mensaje del commit"

# =========================
# SUBIR A GITHUB
# =========================
git push origin main
git push -u origin main

# =========================
# ACTUALIZAR REPO LOCAL
# =========================
git pull origin main

# =========================
# RAMAS (BRANCHES)
# =========================
git branch
git branch nombre-rama
git checkout nombre-rama
git checkout -b nombre-rama
git merge nombre-rama

# Eliminar ramas
git branch -d nombre-rama
git push origin --delete nombre-rama

# =========================
# HISTORIAL
# =========================
git log
git log --oneline

# =========================
# REMOTOS
# =========================
git remote add origin https://github.com/usuario/repositorio.git
git remote -v

# =========================
# DESHACER CAMBIOS
# =========================
git reset archivo.txt
git checkout -- archivo.txt

# =========================
# STASH (GUARDAR TEMPORAL)
# =========================
git stash
git stash pop
