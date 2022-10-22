# language: es
Característica:
  Como usuario certificado de salesforce
  quiero realizar la activación de cuentas residenciales
  para que cuenten con los servicios especificados en el plan

  Esquema del escenario: Activación Residencial
    Dado que soy un usuario certificado de Salesforce '<Escenario>'
    Cuando realizo la limpieza de los equipos
    Y verifico que la ONT se encuentre en autofind
    Y genero el modelado de la cuenta
    Y obtengo el DN del front
    Y realizo una correcta limpieza del DN
    Y realizo la activacion de la cuenta
    Entonces recibo un mensaje indicando que la activacion fue exitosa

    Ejemplos:
      | Escenario |
      | 0         |
      | 1         |
      | 2         |
      | 3         |
      | 4         |
      | 5         |
      | 6         |
      | 7         |
      | 8         |
      | 9         |
      | 10        |
      | 11        |
      | 12        |
      | 13        |
      | 14        |
      | 15        |
      | 16        |
      | 17        |
      | 18        |
      | 19        |