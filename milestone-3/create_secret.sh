ROOTUSER=`echo -n 'admin' | base64`
USER=`echo -n 'empuser' | base64`
PASS=`echo -n 'password' | base64`
cat > 02-secret.yaml <<EOF
apiVersion: v1
kind: Secret
metadata:
  name: mysqlroot
  namespace: liveproject
type: Opaque
data:
  username: $ROOTUSER
  password: $PASS
---
apiVersion: v1
kind: Secret
metadata:
  name: mysqluser
  namespace: liveproject
type: Opaque
data:
  username: $USER
  password: $PASS
EOF
